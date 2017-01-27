package at.itbh.bev.jpa;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Fields;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.SortableField;
import org.hibernate.search.annotations.Spatial;
import org.hibernate.search.annotations.SpatialMode;
import org.hibernate.search.spatial.Coordinates;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import at.itbh.bev.api.data.AustrianCommonAddress;
import at.itbh.bev.api.data.BevAddress;
import at.itbh.bev.index.AddressLineExactMatchAnalyzer;
import at.itbh.bev.index.HouseIdAnalyzer;
import at.itbh.bev.index.HouseIdExactMatchAnalyzer;
import at.itbh.bev.index.PlaceFilterFactory;
import at.itbh.bev.index.PostalCodeAnalyzer;
import at.itbh.bev.index.RegionFilterFactory;
import at.itbh.bev.index.TextAnalyzer;

/**
 * The persistent class for the ADRESSE_DENORMALIZED database table.
 * 
 * <p>
 * The underlying table is computed on data import. <code>BEV.ADRESSE</code> is
 * joined with <code>BEV.STRASSE</code>, <code>BEV.ORTSCHAFT</code>,
 * <code>BEV.GEMEINDE</code> and <code>BEV.Gebaeude</code> to allow for
 * immediate access to a human readable address.
 * </p>
 */
@Entity
@Indexed
@EntityListeners(PreventAnyUpdate.class)
@Table(name = "ADRESSE_DENORMALIZED", schema = "BEV")
@NamedQueries({ @NamedQuery(name = "AdresseDenormalized.findAll", query = "SELECT a FROM AdresseDenormalized a"),
		@NamedQuery(name = "AdresseDenormalized.findByADRCD", query = "SELECT a FROM AdresseDenormalized a WHERE a.adrcd = :adrcd") })
@FullTextFilterDefs({ @FullTextFilterDef(name = "placeFilter", impl = PlaceFilterFactory.class),
		@FullTextFilterDef(name = "regionFilter", impl = RegionFilterFactory.class) })
@Analyzer(impl = TextAnalyzer.class)
public class AdresseDenormalized implements Serializable, BevAddress, AustrianCommonAddress {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String adrcd;

	private String bestimmungsart;

	@Column(name = "BESTIMMUNGSART_GEBAEUDE")
	private String bestimmungsartGebaeude;

	private String eigenschaft;

	private String epsg;

	@Column(name = "EPSG_GEBAEUDE")
	private String epsgGebaeude;

	private String gemeindename;

	private String gkz;

	private String gnradresse;

	private String hauptadresse;

	private String hausnrbereich;

	private String hausnrbuchstabe1;

	private String hausnrbuchstabe2;

	private String hausnrbuchstabe3;

	private String hausnrbuchstabe4;

	private String hausnrgebaeudebez;

	private String hausnrtext;

	private String hausnrverbindung1;

	private String hausnrverbindung2;

	private String hausnrverbindung3;

	private String hausnrzahl1;

	private String hausnrzahl2;

	private String hausnrzahl3;

	private String hausnrzahl4;

	private String hofname;

	private String hw;

	@Column(name = "HW_GEBAEUDE")
	private String hwGebaeude;

	private String objektnummer;

	private String okz;

	private String ortsname;

	private String plz;

	private String quelladresse;

	@Column(name = "QUELLADRESSE_GEBAEUDE")
	private String quelladresseGebaeude;

	private String rw;

	@Column(name = "RW_GEBAEUDE")
	private String rwGebaeude;

	private String skz;

	private String strassenname;

	private String strassennamenzusatz;

	private String subcd;

	private String szusadrbest;

	private String zaehlsprengel;

	/**
	 * Cache for the latitude
	 */
	@Transient
	private Double latitude;

	/**
	 * Cache for the longitude
	 */
	@Transient
	private Double longitude;

	@Fields({ @Field(name = "houseId", analyzer = @Analyzer(impl = HouseIdAnalyzer.class)),
			@Field(name = "houseIdExact", analyzer = @Analyzer(impl = HouseIdExactMatchAnalyzer.class)) })
	public String getHouseId() {
		return Objects.toString(getHausnrtext(), "") + Objects.toString(getHausnrzahl1(), "")
				+ Objects.toString(getHausnrbuchstabe1(), "") + Objects.toString(getHausnrverbindung1(), "")
				+ Objects.toString(getHausnrzahl2(), "") + Objects.toString(getHausnrbuchstabe2(), "")
				+ Objects.toString(getHausnrverbindung2(), "") + Objects.toString(getHausnrzahl3(), "")
				+ Objects.toString(getHausnrbuchstabe3(), "") + Objects.toString(getHausnrverbindung3(), "")
				+ Objects.toString(getHausnrzahl4(), "") + Objects.toString(getHausnrbuchstabe4(), "");
	}

	/**
	 * Combine street and house and address names in one single indexable field
	 * 
	 * @return a concatenated representation of {@link #getStrassenname()},
	 *         {@link #getStrassennamenzusatz()}, {@link #getHofname()} and
	 *         {@link #getHausnrgebaeudebez()}
	 */
	@Fields({ 
		@Field(name = "addressLine", analyzer = @Analyzer(impl = TextAnalyzer.class)),
		@Field(name = "addressLineExact", analyzer = @Analyzer(impl = AddressLineExactMatchAnalyzer.class))
	})
	public String getAddressLine() {
		String fullStreet = Objects.toString(strassenname, "") + Objects.toString(strassennamenzusatz, "");
		// return hofname or String supplied as parameter s
		Function<String, String> myHofname = (s) -> {
			if (Objects.toString(getHofname(), "").length() > 0)
				return getHofname();
			return s;
		};
		// return hausnrgebaeudebez or String supplied as parameter s
		Function<String, String> myHausnrgebaeudebez = (s) -> {
			if (Objects.toString(getHausnrgebaeudebez(), "").length() > 0)
				return getHausnrgebaeudebez();
			return s;
		};
		String hofname = myHofname.apply(fullStreet);
		String gebaeudename = myHausnrgebaeudebez.apply(fullStreet);
		return fullStreet + hofname + gebaeudename;
	}

	@Field
	@Analyzer(impl = PostalCodeAnalyzer.class)
	public String getPostalCode() {
		return plz;
	}

	@Fields({ @Field(name = "municipality", analyzer = @Analyzer(impl = TextAnalyzer.class)),
			@Field(name = "municipalityExact", analyzer = @Analyzer(impl = KeywordAnalyzer.class)) })
	public String getMunicipality() {
		return gemeindename;
	}

	@Fields({ @Field(name = "place", analyzer = @Analyzer(impl = TextAnalyzer.class)),
			@Field(name = "placeExact", analyzer = @Analyzer(impl = KeywordAnalyzer.class)) })
	public String getPlace() {
		return ortsname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getId()
	 */

	public String getId() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getAdrcd()
	 */

	public String getAdrcd() {
		return this.adrcd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getBestimmungsart()
	 */

	public String getBestimmungsart() {
		return this.bestimmungsart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getBestimmungsartGebaeude()
	 */

	public String getBestimmungsartGebaeude() {
		return this.bestimmungsartGebaeude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getEigenschaft()
	 */

	public String getEigenschaft() {
		return this.eigenschaft;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getEpsg()
	 */

	public String getEpsg() {
		return this.epsg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getEpsgGebaeude()
	 */

	public String getEpsgGebaeude() {
		return this.epsgGebaeude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getGemeindename()
	 */

	public String getGemeindename() {
		return this.gemeindename;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getGkz()
	 */

	public String getGkz() {
		return this.gkz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getGnradresse()
	 */

	public String getGnradresse() {
		return this.gnradresse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHauptadresse()
	 */

	public String getHauptadresse() {
		return this.hauptadresse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrbereich()
	 */

	public String getHausnrbereich() {
		return this.hausnrbereich;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrbuchstabe1()
	 */

	public String getHausnrbuchstabe1() {
		return this.hausnrbuchstabe1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrbuchstabe2()
	 */

	public String getHausnrbuchstabe2() {
		return this.hausnrbuchstabe2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrbuchstabe3()
	 */

	public String getHausnrbuchstabe3() {
		return this.hausnrbuchstabe3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrbuchstabe4()
	 */

	public String getHausnrbuchstabe4() {
		return this.hausnrbuchstabe4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrgebaeudebez()
	 */

	@Field
	@Analyzer(impl = TextAnalyzer.class)
	public String getHausnrgebaeudebez() {
		return this.hausnrgebaeudebez;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrtext()
	 */

	public String getHausnrtext() {
		return this.hausnrtext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrverbindung1()
	 */

	public String getHausnrverbindung1() {
		return this.hausnrverbindung1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrverbindung2()
	 */

	public String getHausnrverbindung2() {
		return this.hausnrverbindung2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrverbindung3()
	 */

	public String getHausnrverbindung3() {
		return this.hausnrverbindung3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrzahl1()
	 */
	@Field(name = "hausnrzahl", analyzer = @Analyzer(impl = KeywordAnalyzer.class))
	public String getHausnrzahl1() {
		return this.hausnrzahl1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrzahl2()
	 */

	public String getHausnrzahl2() {
		return this.hausnrzahl2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrzahl3()
	 */

	public String getHausnrzahl3() {
		return this.hausnrzahl3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHausnrzahl4()
	 */
	@Field(name = "houseNumber4", analyzer = @Analyzer(impl = KeywordAnalyzer.class))
	public String getHausnrzahl4() {
		return this.hausnrzahl4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHofname()
	 */

	@Field
	@Analyzer(impl = TextAnalyzer.class)
	public String getHofname() {
		return this.hofname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHw()
	 */

	public String getHw() {
		return this.hw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getHwGebaeude()
	 */

	public String getHwGebaeude() {
		return this.hwGebaeude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getObjektnummer()
	 */

	public String getObjektnummer() {
		return this.objektnummer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getOkz()
	 */

	public String getOkz() {
		return this.okz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getOrtsname()
	 */

	public String getOrtsname() {
		return this.ortsname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getPlz()
	 */

	public String getPlz() {
		return this.plz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getQuelladresse()
	 */

	public String getQuelladresse() {
		return this.quelladresse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getQuelladresseGebaeude()
	 */

	public String getQuelladresseGebaeude() {
		return this.quelladresseGebaeude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getRw()
	 */

	public String getRw() {
		return this.rw;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getRwGebaeude()
	 */

	public String getRwGebaeude() {
		return this.rwGebaeude;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getSkz()
	 */

	public String getSkz() {
		return this.skz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getStrassenname()
	 */

	public String getStrassenname() {
		return this.strassenname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getStrassennamenzusatz()
	 */

	public String getStrassennamenzusatz() {
		return this.strassennamenzusatz;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getSubcd()
	 */

	public String getSubcd() {
		return this.subcd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getSzusadrbest()
	 */

	public String getSzusadrbest() {
		return this.szusadrbest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.itbh.bev.jpa.AdresseAPI#getZaehlsprengel()
	 */

	public String getZaehlsprengel() {
		return this.zaehlsprengel;
	}

	/**
	 * Fetch the most specific y component of the used coordinate system.
	 * 
	 * <p>
	 * If there are coordinates of a building (database table
	 * <code>BEV.GEBAEUDE</code>) available these ones will be used. Otherwise
	 * the coordinates of the address (database table <code>BEV.ADRESSE</code>)
	 * will be used.
	 * </p>
	 * 
	 * @return the y component of the coordinate
	 * @exception NumberFormatException
	 *                the x component could not be transformed to a
	 *                {@link Double} value
	 */
	private Double getMostSpecificHw() throws NumberFormatException {
		if (hwGebaeude != null && hwGebaeude.length() > 0 && rwGebaeude != null && rwGebaeude.length() > 0
				&& epsgGebaeude != null && epsgGebaeude.length() > 0) {
			return Double.parseDouble(hwGebaeude);
		} else {
			return Double.parseDouble(hw);
		}
	}

	/**
	 * Fetch the most specific x component of the used coordinate system.
	 * 
	 * <p>
	 * If there are coordinates of a building (database table
	 * <code>BEV.GEBAEUDE</code>) available these ones will be used. Otherwise
	 * the coordinates of the address (database table <code>BEV.ADRESSE</code>)
	 * will be used.
	 * </p>
	 * 
	 * @return the x component of the coordinate
	 * @exception NumberFormatException
	 *                the x component could not be transformed to a
	 *                {@link Double} value
	 */
	private Double getMostSpecificRw() throws NumberFormatException {
		if (hwGebaeude != null && hwGebaeude.length() > 0 && rwGebaeude != null && rwGebaeude.length() > 0
				&& epsgGebaeude != null && epsgGebaeude.length() > 0) {
			return Double.parseDouble(rwGebaeude);
		} else {
			return Double.parseDouble(rw);
		}
	}

	/**
	 * Fetch the most specific geographic projection.
	 * 
	 * <p>
	 * If there are coordinates of a building (database table
	 * <code>BEV.GEBAEUDE</code>) available their projection will be used.
	 * Otherwise the projection of the coordinates of the address (database
	 * table <code>BEV.ADRESSE</code>) will be used.
	 * </p>
	 * 
	 * @return the x component of the coordinate
	 */
	private String getMostSpecificEpsg() {
		if (hwGebaeude != null && hwGebaeude.length() > 0 && rwGebaeude != null && rwGebaeude.length() > 0
				&& epsgGebaeude != null && epsgGebaeude.length() > 0) {
			return epsgGebaeude;
		} else {
			return epsg;
		}
	}

	@Override
	public Double getLatitude() {
		return getLocation().getLatitude();
	}

	@Override
	public Double getLongitude() {
		return getLocation().getLongitude();
	}

	@SortableField
	@Spatial(spatialMode = SpatialMode.HASH)
	public Coordinates getLocation() {
		return new Coordinates() {
			/**
			 * <p>
			 * On the first invocation of {@link #getLatitude()} or
			 * {@link #getLongitude()} the coordinates are computed and cached.
			 * Any subsequent update of the entity won't trigger a
			 * re-computation.
			 * </p>
			 * 
			 * @return the most specific latitude or <code>null</code> if the
			 *         latitude cannot be computed.
			 */
			public Double getLatitude() {
				if (latitude != null)
					return latitude;

				computeGpsCoordinates();
				return latitude;
			}

			/**
			 * <p>
			 * On the first invocation of {@link #getLatitude()} or
			 * {@link #getLongitude()} the coordinates are computed and cached.
			 * Any subsequent update of the entity won't trigger a
			 * re-computation.
			 * </p>
			 * 
			 * @return the most specific longitude or <code>null</code> if the
			 *         longitude cannot be computed.
			 */
			public Double getLongitude() {
				if (longitude != null)
					return longitude;

				computeGpsCoordinates();
				return longitude;
			}
		};
	}

	/**
	 * Compute the most specific GPS coordinates and cache them in the fields
	 * {@link #latitude} and {@link #longitude}.
	 */
	private void computeGpsCoordinates() {
		try {
			Double rw = getMostSpecificRw();
			Double hw = getMostSpecificHw();

			CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + getMostSpecificEpsg().trim(), true);
			CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326"); // GPS;
																			// WGS84

			GeometryFactory geo = new GeometryFactory();
			Point p = geo.createPoint(new Coordinate(rw, hw));

			MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
			Geometry geographicBounds = JTS.transform(p, transform);

			this.longitude = geographicBounds.getCoordinate().y;
			this.latitude = geographicBounds.getCoordinate().x;
		} catch (Exception e) {
			this.longitude = null;
			this.latitude = null;
		}
	}

	public String toString() {
		return "AdresseDenormalized [id=" + id + ", adrcd=" + adrcd + ", bestimmungsart=" + bestimmungsart
				+ ", bestimmungsartGebaeude=" + bestimmungsartGebaeude + ", eigenschaft=" + eigenschaft + ", epsg="
				+ epsg + ", epsgGebaeude=" + epsgGebaeude + ", gemeindename=" + gemeindename + ", gkz=" + gkz
				+ ", gnradresse=" + gnradresse + ", hauptadresse=" + hauptadresse + ", hausnrbereich=" + hausnrbereich
				+ ", hausnrbuchstabe1=" + hausnrbuchstabe1 + ", hausnrbuchstabe2=" + hausnrbuchstabe2
				+ ", hausnrbuchstabe3=" + hausnrbuchstabe3 + ", hausnrbuchstabe4=" + hausnrbuchstabe4
				+ ", hausnrgebaeudebez=" + hausnrgebaeudebez + ", hausnrtext=" + hausnrtext + ", hausnrverbindung1="
				+ hausnrverbindung1 + ", hausnrverbindung2=" + hausnrverbindung2 + ", hausnrverbindung3="
				+ hausnrverbindung3 + ", hausnrzahl1=" + hausnrzahl1 + ", hausnrzahl2=" + hausnrzahl2 + ", hausnrzahl3="
				+ hausnrzahl3 + ", hausnrzahl4=" + hausnrzahl4 + ", hofname=" + hofname + ", hw=" + hw + ", hwGebaeude="
				+ hwGebaeude + ", objektnummer=" + objektnummer + ", okz=" + okz + ", ortsname=" + ortsname + ", plz="
				+ plz + ", quelladresse=" + quelladresse + ", quelladresseGebaeude=" + quelladresseGebaeude + ", rw="
				+ rw + ", rwGebaeude=" + rwGebaeude + ", skz=" + skz + ", strassenname=" + strassenname
				+ ", strassennamenzusatz=" + strassennamenzusatz + ", subcd=" + subcd + ", szusadrbest=" + szusadrbest
				+ ", zaehlsprengel=" + zaehlsprengel + ", latitude=" + latitude + ", longitude=" + longitude + "]";
	}

	@Override
	public String getStreet() {
		if (Objects.toString(getSzusadrbest(), "").equals("1"))
			return (getStrassenname() + " " + getStrassennamenzusatz()).replaceAll(" +", " ").trim();
		return getStrassenname();
	}

	@Override
	public String getBuildingName() {
		return getHausnrgebaeudebez();
	}

	@Override
	public String getAddressName() {
		return getHofname();
	}

	@Override
	public Integer getHouseNumber() {
		if (Objects.toString(getHausnrtext(), "").length() > 0)
			return null;
		try {
			return Integer.parseInt(getHausnrzahl1());
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getHouseNumberAddition() {
		String addition = "";
		if (Objects.toString(getHausnrtext(), "").length() > 0)
			addition += getHausnrtext() + " " + Objects.toString(getHausnrzahl1(), "");
		addition += Objects.toString(getHausnrverbindung1(), "") + Objects.toString(getHausnrzahl2(), "");
		return addition.replaceAll(" +", " ").trim();
	}

	@Override
	public String getBuildingId() {
		String id = Objects.toString(getHausnrverbindung2(), "") + " " + Objects.toString(getHausnrzahl3(), "")
				+ " " + Objects.toString(getHausnrverbindung3(), "") + " " + Objects.toString(getHausnrzahl4(), "");
		return id.replaceAll(" +", " ").replaceAll("Stiege", " ").replaceAll("Stg.", " ").replaceAll("Stg", " ").trim();
	}
}
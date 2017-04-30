/* 
 * BevAddress.java
 *  
 * Copyright (C) 2017 Christoph D. Hermann <christoph.hermann@itbh.at>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.itbh.bev.api.data;

/**
 * A full address is the combination of data in the <code>ADRESSE</code> and
 * <code>GEBAEUDE</code> tables.
 * 
 * <p>
 * For full details of the meaning of the fields please refer to the BEV data
 * documentation provided with each ZIP file at <a href=
 * "http://www.bev.gv.at/portal/page?_pageid=713,2601271&_dad=portal&_schema=
 * PORTAL">http://www.bev.gv.at/portal/page?_pageid=713,2601271&_dad=portal&
 * _schema=PORTAL</a>.
 * </p>
 * 
 * <p>
 * {@link BevAddress} goes online down to the building level. It never
 * contains apartment information.
 * </p>
 */
public interface BevAddress extends Address {

	String getBestimmungsart();

	String getBestimmungsartGebaeude();

	String getEigenschaft();

	String getEpsg();

	String getEpsgGebaeude();

	String getGemeindename();

	String getGkz();

	String getGnradresse();

	String getHauptadresse();

	String getHausnrbereich();

	String getHausnrbuchstabe1();

	String getHausnrbuchstabe2();

	String getHausnrbuchstabe3();

	String getHausnrbuchstabe4();

	String getHausnrgebaeudebez();

	String getHausnrtext();

	String getHausnrverbindung1();

	String getHausnrverbindung2();

	String getHausnrverbindung3();

	String getHausnrzahl1();

	String getHausnrzahl2();

	String getHausnrzahl3();

	String getHausnrzahl4();

	String getHofname();

	String getHw();

	String getHwGebaeude();

	String getObjektnummer();

	String getOkz();

	String getOrtsname();

	String getPlz();

	String getQuelladresse();

	String getQuelladresseGebaeude();

	String getRw();

	String getRwGebaeude();

	String getSkz();

	String getStrassenname();

	String getStrassennamenzusatz();
	
	String getSzusadrbest();

	String getZaehlsprengel();

}
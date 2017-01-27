// Copyright (c) 2016, DI Christoph D. Hermann (ITBH). All rights reserved. Use of this source code
// is governed by a BSD-style license that can be found in the LICENSE file.
@HtmlImport('main_app.html')
library bev_client.lib.main_app;

import 'dart:html';
import 'dart:convert';

import 'package:polymer/polymer.dart';
import 'package:web_components/web_components.dart';

import 'package:polymer_elements/paper_input.dart';
import 'package:polymer_elements/paper_button.dart';
import 'package:polymer_elements/paper_toast.dart';
import 'package:polymer_elements/paper_item.dart';
import 'package:polymer_elements/paper_dialog.dart';
import 'package:polymer_elements/paper_icon_button.dart';

import 'package:polymer_elements/iron_icon.dart';
import 'package:polymer_elements/iron_icons.dart';
import 'package:polymer_elements/iron_ajax.dart';
import 'package:polymer_elements/iron_collapse.dart';
import 'package:polymer_elements/iron_flex_layout.dart';

import 'package:polymer_elements/maps_icons.dart';

import 'package:polymer_elements/google_map.dart';

@PolymerRegister('main-app')
class MainApp extends PolymerElement {
  
  @property
  String postalCode;

  @property
  String place;

  @property
  String addressLine;

  @property
  String houseId;

  @Property(notify: true)
  String latitude;

  @Property(notify: true)
  String longitude;

  @Property(notify: true)
  String radius;

  @property
  String restUrl = "/bev-search-rest/v1/at/common/geocode";
  // String restUrl = "http://localhost:8080/bev-search-rest/v1/at/common/geocode";
  
  @Property(notify: true)
  String restUrlReverse = "/bev-search-rest/v1/at/common/geocode/#latitude#/#longitude#/#radius#";
  // String restUrlReverse = "http://localhost:8080/bev-search-rest/v1/at/common/geocode/#latitude#/#longitude#/#radius#";
  
  @property
  var restParams = {};

  @property
  String lastError;

  @Property(notify: true)
  var resultList = [];

  @Property(notify: true)
  String selectedLatitude;

  @Property(notify: true)
  String selectedLongitude;

  @Property(notify: true)
  String selectedAddress;

  static const String SUPPORTED_RESPONSE_TYPE = "at.itbh.bev.api.data.AustrianCommonQueryResult";

  void triggerSearch() {
    showLoadingIndicator();
    
    IronAjax ajaxGeocode = $$("#ajaxGeocode");
    IronAjax ajaxReverseGeocode = $$("#ajaxReverseGeocode");

    int geocodeCount = 0;
    
    bool hasLatitude = false;
    bool hasLongitude = false;
    bool hasRadius = false;

    var myParams = {};
    restParams = {};
    if (postalCode != null && postalCode != "") {
      myParams['postalCode'] = postalCode;
      geocodeCount++;
    }
    if (place != null && place != "") {
      myParams['place'] = place;
      geocodeCount++;
    }
    if (place != null && place != "") {
      myParams['place'] = place;
      geocodeCount++;
    }
    if (addressLine != null && addressLine != "") {
      myParams['addressLine'] = addressLine;
      geocodeCount++;
    }
    if (houseId != null && houseId != "") {
      myParams['houseId'] = houseId;
      geocodeCount++;
    }
    if (latitude != null && latitude != "") {
      myParams['latitude'] = latitude.replaceAll(',', '.');
      set('latitude', myParams['latitude']);
      hasLatitude = true;
    }
    if (longitude != null && longitude != "") {
      myParams['longitude'] = longitude.replaceAll(',', '.');
      set('longitude', myParams['longitude']);
      hasLongitude = true;
    }
    if (radius != null && radius != "") {
      myParams['radius'] = radius.replaceAll(',', '.');
      set('radius', myParams['radius']);
      hasRadius = true;
    }

    set('restParams', myParams);

    String currentReverseRestURL = restUrlReverse;
    
    if (geocodeCount == 0 && hasLatitude && hasLongitude && !hasRadius) {
      set('restUrlReverse', restUrlReverse.replaceAll('#latitude#', latitude).replaceAll('#longitude#', longitude).replaceAll('#radius#', ""));
      print("Reverse geocoding with default radius: $restUrlReverse");
      ajaxReverseGeocode.generateRequest();
    }
    else  if (geocodeCount == 0 && hasLatitude && hasLongitude && hasRadius) {
      set('restUrlReverse', restUrlReverse.replaceAll('#latitude#', latitude).replaceAll('#longitude#', longitude).replaceAll('#radius#', radius));
      print("Reverse geocoding with radius $radius: $restUrlReverse");
      ajaxReverseGeocode.generateRequest();
    }
    else {
      print("Geocoding params: $restParams");
      ajaxGeocode.generateRequest();
    }

    set('restUrlReverse', currentReverseRestURL);
  }

  @reflectable
  void onSubmitSearchForm(Event e, var detail) {
    e.preventDefault();
    triggerSearch();
  }
  
  @reflectable
  void search(Event e, var detail) {
    triggerSearch();
  }

  void showError(String message) {
    set('lastError', message);
    PaperToast errorToast = $$("#errorToast");
    errorToast.open();
  }

  void removeLastError() {
    set('lastError', null);
    PaperToast errorToast = $$("#errorToast");
    errorToast.close();
  }

  void removeLastResults() {
    set('resultList', []);
  }

  void showResults(var results) {
    set('resultList', results);
  }

  void hideLoadingIndicator() {
    $$('#search-button-icon').icon = "search";
    $$('#loading-indicator').classes.toggle('hidden');
  }

  void showLoadingIndicator() {
    $$('#search-button-icon').icon = "refresh";
    $$('#loading-indicator').classes.toggle('hidden');
  }

  @reflectable
    void showMapDialog(Event e, var detail) {
    set('selectedLatitude', e.currentTarget.dataset['latitude']);
    set('selectedLongitude', e.currentTarget.dataset['longitude']);
    set('selectedAddress', e.currentTarget.dataset['address']);
    $$('#mapDialog').open();
  }

  @reflectable
  void toggleResultDetail(Event e, var detail) {
    IronIcon element = e.currentTarget;
    String collapseId = element.dataset['collapseId'];
    IronCollapse collapse = $$("#$collapseId");

    collapse.toggleClass("hidden");
    collapse.toggle();

    if (element.icon == "arrow-drop-down") {
      element.icon = "arrow-drop-up";
    }
    else {
      element.icon = "arrow-drop-down";
    }
  }

  @reflectable
  void handleGeocodeResponse(Event e, var detail) {
    removeLastError();
    removeLastResults();
    int status = detail.response['status']['code'];
    if (status > 0)
      showError(detail.response['status']['message']);
    if (detail.response['status']['responseType'] != SUPPORTED_RESPONSE_TYPE) {
      showError("Only response type '$SUPPORTED_RESPONSE_TYPE' is supported.");
    }
    showResults(detail.response['response']);
    hideLoadingIndicator();
  }

  @reflectable
  void handleGeocodeError(Event e, var detail) {
    showError(detail['error']);
    hideLoadingIndicator();
  }

    
  /// Constructor used to create instance of MainApp.
  MainApp.created() : super.created();

  // Optional lifecycle methods - uncomment if needed.

//  /// Called when an instance of main-app is inserted into the DOM.
//  attached() {
//    super.attached();
//  }

//  /// Called when an instance of main-app is removed from the DOM.
//  detached() {
//    super.detached();
//  }

//  /// Called when an attribute (such as a class) of an instance of
//  /// main-app is added, changed, or removed.
//  attributeChanged(String name, String oldValue, String newValue) {
//    super.attributeChanged(name, oldValue, newValue);
//  }

//  /// Called when main-app has been fully prepared (Shadow DOM created,
//  /// property observers set up, event listeners attached).
//  ready() {
//  }
}

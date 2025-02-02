/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.radiology;

import org.openmrs.CareSetting;
import org.openmrs.ConceptClass;
import org.openmrs.EncounterRole;
import org.openmrs.EncounterType;
import org.openmrs.OrderType;
import org.openmrs.module.emrapi.utils.ModuleProperties;
import org.springframework.stereotype.Component;

/**
 * Properties, mostly configured via GPs for this module.
 */
@Component
public class RadiologyProperties extends ModuleProperties {
	
	/**
	 * Return application entity title
	 * 
	 * @return application entity title
	 */
	public String getApplicationEntityTitle() {
		return getGlobalProperty(RadiologyConstants.GP_APPLICATION_ENTITY_TITLE, true);
	}
	
	/**
	 * Return mpps directory
	 * 
	 * @return mpps directory
	 */
	public String getMppsDir() {
		return getGlobalProperty(RadiologyConstants.GP_MPPS_DIR, true);
	}
	
	/**
	 * Return mwl directory
	 * 
	 * @return mwl directory
	 */
	public String getMwlDir() {
		return getGlobalProperty(RadiologyConstants.GP_MWL_DIR, true);
	}
	
	/**
	 * Return mwl mpps port
	 * 
	 * @return mwl mpps port
	 */
	public String getMwlMppsPort() {
		return getGlobalProperty(RadiologyConstants.GP_MWL_MPPS_PORT, true);
	}
	
	/**
	 * Return Server Address
	 * 
	 * @return server address
	 */
	public String getServersAddress() {
		return "http://" + getGlobalProperty(RadiologyConstants.GP_SERVERS_ADDRESS, true);
	}
	
	/**
	 * Return prefix for dicom objects in the application, Ex: 1.2.826.0.1.3680043.8.2186
	 * 
	 * @return prefix for dicom objects in the application
	 */
	public String getApplicationUID() {
		return getGlobalProperty(RadiologyConstants.GP_APPLICATION_UID, true);
	}
	
	/**
	 * Return study uid slug
	 * 
	 * @return study uid slug
	 */
	public String getStudyUIDSlug() {
		return getGlobalProperty(RadiologyConstants.GP_STUDY_UID_SLUG, true);
	}
	
	/**
	 * Return specific character set
	 * 
	 * @return specific character set
	 */
	public String getSpecificCharacterSet() {
		return getGlobalProperty(RadiologyConstants.GP_SPECIFIC_CHARCATER_SET, true);
	}
	
	/**
	 * Return study prefix Example: 1.2.826.0.1.3680043.8.2186.1. (With last dot)
	 * 
	 * @return study prefix
	 * @should return study prefix consisting of application uid and study uid slug
	 */
	public String getStudyPrefix() {
		return getApplicationUID() + "." + getStudyUIDSlug() + ".";
	}
	
	/**
	 * Return servers port
	 * 
	 * @return servers port
	 */
	public String getServersPort() {
		return getGlobalProperty(RadiologyConstants.GP_SERVERS_PORT, true);
	}
	
	/**
	 * Return servers hl7 port
	 * 
	 * @return servers hl7 port
	 * @should return port of the dcm4chee hl7 receiver/sender
	 */
	public String getServersHL7Port() {
		return getGlobalProperty(RadiologyConstants.GP_SERVERS_HL7_PORT, true);
	}
	
	/**
	 * Return server name of local dicom viewer
	 * 
	 * @return server name of local dicom viewer
	 * @should return dicom viewer local server name if defined in global properties
	 * @should return empty string if dicom viewer local server name if not defined in global
	 *         properties
	 */
	public String getDicomViewerLocalServerName() {
		final String dicomViewerLocalServerName = getGlobalProperty(RadiologyConstants.GP_DICOM_VIEWER_LOCAL_SERVER_NAME,
		    false);
		return dicomViewerLocalServerName == null ? "" : "serverName=" + dicomViewerLocalServerName + "&";
	}
	
	/**
	 * Return dicom viewer url base
	 * 
	 * @return dicom viewer url base
	 */
	public String getDicomViewerUrlBase() {
		return getGlobalProperty(RadiologyConstants.GP_DICOM_VIEWER_URL_BASE, true);
	}
	
	/**
	 * Return url of dicom viewer
	 * 
	 * @return url of dicom viewer as string
	 * @should return dicom viewer url consisting of server adress and server port and dicom viewer
	 *         url base and dicom viewer local server name for oviyam
	 * @should return dicom viewer url consisting of server adress and server port and dicom viewer
	 *         url base and dicom viewer local server name for weasis
	 */
	public String getDicomViewerUrl() {
		final String dicomViewerUrl = getServersAddress() + ":" + getServersPort() + getDicomViewerUrlBase()
		        + getDicomViewerLocalServerName();
		return dicomViewerUrl;
	}
	
	/**
	 * Get CareSetting for RadiologyOrder's
	 * 
	 * @return CareSetting for radiology orders
	 * @should return radiology care setting
	 * @should throw illegal state exception if global property for radiology care setting cannot be found
	 * @should throw illegal state exception if radiology care setting cannot be found
	 */
	public CareSetting getRadiologyCareSetting() {
		final String radiologyCareSettingUuid = getGlobalProperty(RadiologyConstants.GP_RADIOLOGY_CARE_SETTING, true);
		final CareSetting result = orderService.getCareSettingByUuid(radiologyCareSettingUuid);
		if (result == null) {
			throw new IllegalStateException("No existing care setting for uuid: "
			        + RadiologyConstants.GP_RADIOLOGY_CARE_SETTING);
		}
		return result;
	}
	
	/**
	 * Test order type for radiology order
	 * 
	 * @return test order type for radiology order
	 * @should return order type for radiology test orders
	 * @should throw illegal state exception for non existing radiology test order type
	 */
	public OrderType getRadiologyTestOrderType() {
		final OrderType result = orderService.getOrderTypeByUuid(RadiologyConstants.RADIOLOGY_TEST_ORDER_TYPE_UUID);
		if (result == null) {
			throw new IllegalStateException("OrderType for radiology orders not in database (not found under uuid="
			        + RadiologyConstants.RADIOLOGY_TEST_ORDER_TYPE_UUID + ").");
		}
		return result;
	}
	
	/**
	 * Get EncounterType for RadiologyOrder's
	 * 
	 * @return EncounterType for radiology orders
	 * @should return encounter type for radiology orders
	 * @should throw illegal state exception for non existing radiology encounter type
	 */
	public EncounterType getRadiologyEncounterType() {
		final EncounterType result = encounterService
		        .getEncounterTypeByUuid(RadiologyConstants.RADIOLOGY_ENCOUNTER_TYPE_UUID);
		if (result == null) {
			throw new IllegalStateException("EncounterType for radiology orders not in database (not found under uuid="
			        + RadiologyConstants.RADIOLOGY_ENCOUNTER_TYPE_UUID + ").");
		}
		return result;
	}
	
	/**
	 * Get EncounterRole for the ordering provider
	 * 
	 * @return EncounterRole for ordering provider
	 * @should return encounter role for ordering provider
	 * @should throw illegal state exception for non existing ordering provider encounter role
	 */
	public EncounterRole getOrderingProviderEncounterRole() {
		final EncounterRole result = encounterService
		        .getEncounterRoleByUuid(RadiologyConstants.ORDERING_PROVIDER_ENCOUNTER_ROLE_UUID);
		if (result == null) {
			throw new IllegalStateException("EncounterRole for ordering provider not in database (not found under uuid="
			        + RadiologyConstants.ORDERING_PROVIDER_ENCOUNTER_ROLE_UUID + ").");
		}
		return result;
	}
	
	/**
	 * Gets the Name of the ConceptClass for the UUID from the config
	 *
	 * @return a String that contains the Names of the ConceptClasses seperated by a comma
	 * @should throw illegal state exception if global property radiologyConceptClasses is null
	 * @should throw illegal state exception if global property radiologyConceptClasses is an empty
	 *         string
	 * @should throw illegal state exception if global property radiologyConceptClasses is badly
	 *         formatted
	 * @should throw illegal state exception if global property radiologyConceptClasses contains a
	 *         UUID not found among ConceptClasses
	 * @should returns comma separated list of ConceptClass names configured via ConceptClass UUIDs
	 *         in global property radiologyConceptClasses
	 */
	public String getRadiologyConceptClassNames() {
		
		String radiologyConceptClassUuidSetting = getGlobalProperty(RadiologyConstants.GP_RADIOLOGY_CONCEPT_CLASSES, true);
		radiologyConceptClassUuidSetting = radiologyConceptClassUuidSetting.replace(" ", "");
		if (!radiologyConceptClassUuidSetting.matches("^[0-9a-fA-f,-]+$")) {
			throw new IllegalStateException(
			        "Property radiology.radiologyConcepts needs to be a comma separated list of concept class UUIDs (allowed characters [a-z][A-Z][0-9][,][-][ ])");
		}
		
		final String[] radiologyConceptClassUuids = radiologyConceptClassUuidSetting.split(",");
		
		String result = "";
		for (String radiologyConceptClassUuid : radiologyConceptClassUuids) {
			ConceptClass fetchedConceptClass = conceptService.getConceptClassByUuid(radiologyConceptClassUuid);
			if (fetchedConceptClass == null) {
				throw new IllegalStateException("Property radiology.radiologyConceptClasses contains UUID "
				        + radiologyConceptClassUuid + " which cannot be found as ConceptClass in the database.");
			}
			result = result + fetchedConceptClass.getName() + ",";
		}
		result = result.substring(0, result.length() - 1);
		return result;
	}
}

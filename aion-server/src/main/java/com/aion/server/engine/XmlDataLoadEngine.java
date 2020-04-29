package com.aion.server.engine;

import java.io.File;
import java.io.FileReader;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.aion.server.model.data.StaticData;

public class XmlDataLoadEngine {
	private static final Logger LOG = LoggerFactory.getLogger(XmlDataLoadEngine.class);

	/**
	 * File containing xml schema declaration
	 */
	private static final String XML_SCHEMA_FILE = "./data/static_data/static_data.xsd";

	private static final String CACHE_DIRECTORY = "./cache/";
	private static final String CACHE_XML_FILE = "./cache/static_data.xml";
	private static final String MAIN_XML_FILE = "./data/static_data/static_data.xml";

	public static XmlDataLoadEngine getInstance() {
		return SingletonHolder.instance;
	}

	private static class SingletonHolder {
		static final XmlDataLoadEngine instance = new XmlDataLoadEngine();
	}

	private XmlDataLoadEngine() {

	}

	/**
	 * Creates {@link StaticData} object based on xml files, starting from static_data.xml
	 *
	 * @return StaticData object, containing all game data defined in xml files
	 */
	public StaticData loadStaticData() {
		makeCacheDirectory();

		File cachedXml = new File(CACHE_XML_FILE);
		File cleanMainXml = new File(MAIN_XML_FILE);

		//mergeXmlFiles(cachedXml, cleanMainXml);

		try {
			JAXBContext jc = JAXBContext.newInstance(StaticData.class);
			Unmarshaller un = jc.createUnmarshaller();
			un.setEventHandler(new DefaultValidationEventHandler());
			un.setSchema(getSchema());
			return (StaticData) un.unmarshal(new FileReader(CACHE_XML_FILE));
		} catch(Exception e) {
			LOG.error("[{}]",e);
			return null;
		}
	}

	/**
	 * Creates and returns {@link Schema} object representing xml schema of xml files
	 *
	 * @return a Schema object.
	 */
	private Schema getSchema() {
		Schema schema = null;
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

		try {
			schema = sf.newSchema(new File(XML_SCHEMA_FILE));
		} catch(SAXException saxe) {
			LOG.error("Error while getting schema", saxe);
			throw new Error("Error while getting schema", saxe);
		}

		return schema;
	}

	/**
	 * Creates directory for cache files if it doesn't already exist
	 */
	private void makeCacheDirectory() {
		File cacheDir = new File(CACHE_DIRECTORY);
		if(!cacheDir.exists())
			cacheDir.mkdir();
	}

	/**
	 * Merges xml files(if are newer than cache file) and puts output to cache file.
	 *
	 * @see XmlMerger
	 * @param cachedXml
	 * @param cleanMainXml
	 * @throws Error
	 *           is thrown if some problem occured.
	 */
   /* private void mergeXmlFiles(File cachedXml, File cleanMainXml) throws Error {
        XmlMerger merger = new XmlMerger(cleanMainXml, cachedXml);
        try {
            merger.process();
        }
        catch (Exception e) {
            LOG.error("Error while merging xml files", e);
            throw new Error("Error while merging xml files", e);
        }
    }*/
}

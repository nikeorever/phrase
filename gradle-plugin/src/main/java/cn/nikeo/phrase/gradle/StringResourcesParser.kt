package cn.nikeo.phrase.gradle

import cn.nikeo.phrase.writer.classes.StringResource
import java.io.File
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants

/**
 * Represent <resource></resources>
 */
private const val XML_ELEMENT_LOCAL_NAME_RESOURCES = "resources"

/**
 * Represent <string></string>
 */
private const val XML_ELEMENT_LOCAL_NAME_STRING = "string"

private const val XML_ELEMENT_ATTRIBUTE_NAME = "name"

fun parseStringResourcesXml(xml: File): List<StringResource>? {
    val reader = XMLInputFactory.newInstance().createXMLStreamReader(xml.inputStream())

    var stringResources: MutableList<StringResource>? = null
    var stringResource: StringResource? = null
    var comment: String? = null

    loop@ while (reader.hasNext()) {
        when (reader.next()) {
            XMLStreamConstants.START_ELEMENT -> {
                val localName = reader.localName
                if (localName == XML_ELEMENT_LOCAL_NAME_RESOURCES) {
                    stringResources = ArrayList()
                } else if (localName == XML_ELEMENT_LOCAL_NAME_STRING) {
                    stringResource = StringResource()
                    stringResource.comment = comment
                    stringResource.sourceXml = xml
                    for (index in 0 until reader.attributeCount) {
                        val attName = reader.getAttributeLocalName(index)
                        val attValue = reader.getAttributeValue(index)
                        if (XML_ELEMENT_ATTRIBUTE_NAME == attName) {
                            stringResource.name = attValue
                        }
                    }
                }
            }
            XMLStreamConstants.CHARACTERS -> {
                stringResource?.textContent = reader.text
            }
            XMLStreamConstants.END_ELEMENT -> {
                val localName = reader.localName
                if (XML_ELEMENT_LOCAL_NAME_RESOURCES == localName) {
                    break@loop
                } else if (XML_ELEMENT_LOCAL_NAME_STRING == localName) {
                    if (stringResources != null && stringResource != null) {
                        // Check if it`s a valid string resource
                        if (stringResource.isPhrase()) {
                            stringResources.add(stringResource)
                        }
                        stringResource = null
                    }
                }

                comment = null
            }
            XMLStreamConstants.COMMENT -> {
                comment = reader.text
            }
        }
    }

    return stringResources
}

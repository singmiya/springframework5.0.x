/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.beans.factory.xml;

import org.w3c.dom.Document;

import org.springframework.beans.factory.BeanDefinitionStoreException;

/**
 * SPI for parsing an XML document that contains Spring bean definitions.
 * Used by {@link XmlBeanDefinitionReader} for actually parsing a DOM document.
 *
 * 解析包含Spring bean定义的XML文档的SPI（为某个接口寻找服务实现的机制）。
 * 由XmlBeanDefinitionReader用来实际解析DOM文档。
 *
 * <p>Instantiated per document to parse: implementations can hold
 * state in instance variables during the execution of the
 * {@code registerBeanDefinitions} method &mdash; for example, global
 * settings that are defined for all bean definitions in the document.
 *
 * 实例化每个要解析的文档：在执行registerBeanDefinitions方法期间，实现可以保存实例变量的状态，例如，为文档中所有的bean definitions 定义的全局设置。
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 18.12.2003
 * @see XmlBeanDefinitionReader#setDocumentReaderClass
 */
public interface BeanDefinitionDocumentReader {

	/**
	 * Read bean definitions from the given DOM document and
	 * register them with the registry in the given reader context.
	 *
	 * 从给定的DOM文档中读取bean定义，并将其注册到给定的读取器上下文中的注册表中。
	 *
	 * @param doc the DOM document
	 * @param readerContext the current context of the reader
	 * (includes the target registry and the resource being parsed)
	 *                      读取器的当前上下文（包含目标注册表和要解析的资源）
	 * @throws BeanDefinitionStoreException in case of parsing errors
	 */
	void registerBeanDefinitions(Document doc, XmlReaderContext readerContext)
			throws BeanDefinitionStoreException;

}

/*
 * Copyright 2002-2017 the original author or authors.
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

import java.io.StringReader;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.ReaderContext;
import org.springframework.beans.factory.parsing.ReaderEventListener;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;

/**
 * Extension of {@link org.springframework.beans.factory.parsing.ReaderContext},
 * specific to use with an {@link XmlBeanDefinitionReader}. Provides access to the
 * {@link NamespaceHandlerResolver} configured in the {@link XmlBeanDefinitionReader}.
 *
 * ReaderContext的扩展，专门用于XmlBeanDefinitionReader。提供对配置在XmlBeanDefinitionReader中的NamespaceHandlerResolver的访问。
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 */
public class XmlReaderContext extends ReaderContext {

	private final XmlBeanDefinitionReader reader;

	private final NamespaceHandlerResolver namespaceHandlerResolver;


	/**
	 * Construct a new {@code XmlReaderContext}.
	 * @param resource the XML bean definition resource
	 * @param problemReporter the problem reporter in use
	 * @param eventListener the event listener in use
	 * @param sourceExtractor the source extractor in use
	 * @param reader the XML bean definition reader in use
	 * @param namespaceHandlerResolver the XML namespace resolver
	 */
	public XmlReaderContext(
			Resource resource, ProblemReporter problemReporter,
			ReaderEventListener eventListener, SourceExtractor sourceExtractor,
			XmlBeanDefinitionReader reader, NamespaceHandlerResolver namespaceHandlerResolver) {

		super(resource, problemReporter, eventListener, sourceExtractor);
		this.reader = reader;
		this.namespaceHandlerResolver = namespaceHandlerResolver;
	}


	/**
	 * Return the XML bean definition reader in use.
	 * 返回使用中的XMLbean定义读取器。
	 */
	public final XmlBeanDefinitionReader getReader() {
		return this.reader;
	}

	/**
	 * Return the bean definition registry to use.
	 * 返回要使用的bean定义注册表。
	 * @see XmlBeanDefinitionReader#XmlBeanDefinitionReader(BeanDefinitionRegistry)
	 */
	public final BeanDefinitionRegistry getRegistry() {
		return this.reader.getRegistry();
	}

	/**
	 * Return the resource loader to use, if any.
	 * <p>This will be non-null in regular scenarios,
	 * also allowing access to the resource class loader.
	 *
	 * 如果存在，返回要使用的资源加载器。
	 * 在正常情况下，该值不为null，并且允许范围资源类加载器。
	 *
	 * @see XmlBeanDefinitionReader#setResourceLoader
	 * @see ResourceLoader#getClassLoader()
	 */
	@Nullable
	public final ResourceLoader getResourceLoader() {
		return this.reader.getResourceLoader();
	}

	/**
	 * Return the bean class loader to use, if any.
	 * <p>Note that this will be null in regular scenarios,
	 * as an indication to lazily resolve bean classes.
	 *
	 * 如果存在，返回要使用的bean类加载器。
	 * 注意，在正常情况下，该值为null，表示延迟解析bean类。
	 *
	 * @see XmlBeanDefinitionReader#setBeanClassLoader
	 */
	@Nullable
	public final ClassLoader getBeanClassLoader() {
		return this.reader.getBeanClassLoader();
	}

	/**
	 * Return the environment to use.
	 * 返回要使用的环境对象。
	 * @see XmlBeanDefinitionReader#setEnvironment
	 */
	public final Environment getEnvironment() {
		return this.reader.getEnvironment();
	}

	/**
	 * Return the namespace resolver.
	 * 返回命名空间解析器。
	 * @see XmlBeanDefinitionReader#setNamespaceHandlerResolver
	 */
	public final NamespaceHandlerResolver getNamespaceHandlerResolver() {
		return this.namespaceHandlerResolver;
	}


	// Convenience methods to delegate to

	/**
	 * Call the bean name generator for the given bean definition.
	 *
	 * 为给定的bean定义调用bean名称生成器。
	 *
	 * @see XmlBeanDefinitionReader#getBeanNameGenerator()
	 * @see org.springframework.beans.factory.support.BeanNameGenerator#generateBeanName
	 */
	public String generateBeanName(BeanDefinition beanDefinition) {
		return this.reader.getBeanNameGenerator().generateBeanName(beanDefinition, getRegistry());
	}

	/**
	 * Call the bean name generator for the given bean definition
	 * and register the bean definition under the generated name.
	 *
	 * 为给定bean定义调用bean名称生成器，并在生成的名字下注册bean定义。
	 *
	 * @see XmlBeanDefinitionReader#getBeanNameGenerator()
	 * @see org.springframework.beans.factory.support.BeanNameGenerator#generateBeanName
	 * @see BeanDefinitionRegistry#registerBeanDefinition
	 */
	public String registerWithGeneratedName(BeanDefinition beanDefinition) {
		String generatedName = generateBeanName(beanDefinition);
		getRegistry().registerBeanDefinition(generatedName, beanDefinition);
		return generatedName;
	}

	/**
	 * Read an XML document from the given String.
	 *
	 * 从给定的字符串中读取XML文档。
	 *
	 * @see #getReader()
	 */
	public Document readDocumentFromString(String documentContent) {
		InputSource is = new InputSource(new StringReader(documentContent));
		try {
			return this.reader.doLoadDocument(is, getResource());
		}
		catch (Exception ex) {
			throw new BeanDefinitionStoreException("Failed to read XML document", ex);
		}
	}

}

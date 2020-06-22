/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.core.io;

import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;

/**
 * Strategy interface for loading resources (e.. class path or file system
 * resources). An {@link org.springframework.context.ApplicationContext}
 * is required to provide this functionality, plus extended
 * {@link org.springframework.core.io.support.ResourcePatternResolver} support.
 *
 * 用于加载资源（如，类文件路径或文件系统资源）的策略接口。
 * 提供此功能需要ApplicationContext，以及扩展的ResourcePatternResolver支持。
 *
 * <p>{@link DefaultResourceLoader} is a standalone implementation that is
 * usable outside an ApplicationContext, also used by {@link ResourceEditor}.
 *
 * DefaultResourceLoader是一个独立的实现，可在ApplicationContext外部使用，也可由ResourceEditor使用。
 *
 * <p>Bean properties of type Resource and Resource array can be populated
 * from Strings when running in an ApplicationContext, using the particular
 * context's resource loading strategy.
 *
 * 在ApplicationContext运行的时候，可以使用特定上下文的资源加载策略从Strings中填充Resource类型的bean属性和Resource数组。
 *
 * @author Juergen Hoeller
 * @since 10.03.2004
 * @see Resource
 * @see org.springframework.core.io.support.ResourcePatternResolver
 * @see org.springframework.context.ApplicationContext
 * @see org.springframework.context.ResourceLoaderAware
 */
public interface ResourceLoader {

	/**
	 * Pseudo URL prefix for loading from the class path: "classpath:"
	 *
	 * 用于从类路径加载的伪URL前缀："classpath:"
	 *
	 */
	String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;


	/**
	 * Return a Resource handle for the specified resource location.
	 *
	 * 为指定的资源位置返回资源（Resource）句柄。
	 *
	 * <p>The handle should always be a reusable resource descriptor,
	 * allowing for multiple {@link Resource#getInputStream()} calls.
	 *
	 * （资源）句柄（handle）应该总是可重用的资源描述符，允许多次调用#getInputStream。
	 *
	 * <p><ul>
	 * <li>Must support fully qualified URLs, e.g. "file:C:/test.dat".  必须支持标准URL格式
	 * <li>Must support classpath pseudo-URLs, e.g. "classpath:test.dat". 必须支持类路径URL为前缀（pseudo-URLs）
	 * <li>Should support relative file paths, e.g. "WEB-INF/test.dat". 应该支持相对文件路径，
	 * (This will be implementation-specific, typically provided by an
	 * ApplicationContext implementation.)
	 *
	 * （他们将分别对应具体的实现，通常情况下由ApplicationContext实现来提供。）
	 *
	 * </ul>
	 * <p>Note that a Resource handle does not imply an existing resource;
	 * you need to invoke {@link Resource#exists} to check for existence.
	 *
	 * 注意，资源句柄并不意味着是已存在的资源；你需要调用#exists来检查资源是否存在。
	 *
	 * @param location the resource location
	 * @return a corresponding Resource handle (never {@code null})
	 * @see #CLASSPATH_URL_PREFIX
	 * @see Resource#exists()
	 * @see Resource#getInputStream()
	 */
	Resource getResource(String location);

	/**
	 * Expose the ClassLoader used by this ResourceLoader.
	 *
	 * 公开ResourceLoader使用的ClassLoader。
	 *
	 * <p>Clients which need to access the ClassLoader directly can do so
	 * in a uniform manner with the ResourceLoader, rather than relying
	 * on the thread context ClassLoader.
	 *
	 * 对于需要直接访问ClassLoader的客户端，可以使用ResourceLoader统一的来操作，而不是依靠线程上下文ClassLoader。
	 *
	 * @return the ClassLoader
	 * (only {@code null} if even the system ClassLoader isn't accessible)
	 * @see org.springframework.util.ClassUtils#getDefaultClassLoader()
	 * @see org.springframework.util.ClassUtils#forName(String, ClassLoader)
	 */
	@Nullable
	ClassLoader getClassLoader();

}

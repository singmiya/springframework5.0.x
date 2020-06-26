/*
 * Copyright 2002-2019 the original author or authors.
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

package org.springframework.core.io.support;

import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.ResourceUtils;

/**
 * Utility class for determining whether a given URL is a resource
 * location that can be loaded via a {@link ResourcePatternResolver}.
 *
 * 用来确定给定的URL是否为可使用ResourcePatternResolver加载的资源位置的实用工具类。
 *
 * <p>Callers will usually assume that a location is a relative path
 * if the {@link #isUrl(String)} method returns {@code false}.
 *
 * 如果isUrl方法返回false，调用者通常会将此位置当做相对路径。
 *
 * @author Juergen Hoeller
 * @since 1.2.3
 */
public abstract class ResourcePatternUtils {

	/**
	 * Return whether the given resource location is a URL: either a
	 * special "classpath" or "classpath*" pseudo URL or a standard URL.
	 *
	 * 返回给定的资源位置是否是URL：要么是特殊"classpath"或"classpath*"这样的伪URL，要么是标准URL。
	 *
	 * @param resourceLocation the location String to check
	 * @return whether the location qualifies as a URL
	 * @see ResourcePatternResolver#CLASSPATH_ALL_URL_PREFIX
	 * @see org.springframework.util.ResourceUtils#CLASSPATH_URL_PREFIX
	 * @see org.springframework.util.ResourceUtils#isUrl(String)
	 * @see java.net.URL
	 */
	public static boolean isUrl(@Nullable String resourceLocation) {
		return (resourceLocation != null &&
				(resourceLocation.startsWith(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX) ||
						ResourceUtils.isUrl(resourceLocation)));
	}

	/**
	 * Return a default {@link ResourcePatternResolver} for the given {@link ResourceLoader}.
	 * <p>This might be the {@code ResourceLoader} itself, if it implements the
	 * {@code ResourcePatternResolver} extension, or a default
	 * {@link PathMatchingResourcePatternResolver} built on the given {@code ResourceLoader}.
	 *
	 * 为给定的ResourceLoader返回默认ResourcePatternResolver。
	 * 如果它的实现扩展自ResourcePatternResolver，返回值可能是ResourceLoader自身，
	 * 或使用给定的ResourceLoader构建一个默认的PathMatchingResourcePatternResolver.
	 *
	 * @param resourceLoader the ResourceLoader to build a pattern resolver for
	 * (may be {@code null} to indicate a default ResourceLoader)
	 * @return the ResourcePatternResolver
	 * @see PathMatchingResourcePatternResolver
	 */
	public static ResourcePatternResolver getResourcePatternResolver(@Nullable ResourceLoader resourceLoader) {
		if (resourceLoader instanceof ResourcePatternResolver) {
			return (ResourcePatternResolver) resourceLoader;
		}
		else if (resourceLoader != null) {
			return new PathMatchingResourcePatternResolver(resourceLoader);
		}
		else {
			return new PathMatchingResourcePatternResolver();
		}
	}

}

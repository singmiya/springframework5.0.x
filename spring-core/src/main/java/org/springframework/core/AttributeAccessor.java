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

package org.springframework.core;

import org.springframework.lang.Nullable;

/**
 * Interface defining a generic contract for attaching and accessing metadata
 * to/from arbitrary objects.
 *
 * 定义用于将元数据附加到任意对象或从任意对象获取元数据的通用协定的接口。
 *
 * @author Rob Harrop
 * @since 2.0
 */
public interface AttributeAccessor {

	/**
	 * Set the attribute defined by {@code name} to the supplied {@code value}.
	 * If {@code value} is {@code null}, the attribute is {@link #removeAttribute removed}.
	 * <p>In general, users should take care to prevent overlaps with other
	 * metadata attributes by using fully-qualified names, perhaps using
	 * class or package names as prefix.
	 *
	 * 为名称定义的属性设置所提供的值。如果值为null，则删除该属性。
	 * 通常，用户应通过使用完全限定名称（可能使用类或包名称作为前缀）来防止与其他元数据属性重叠。
	 *
	 * @param name the unique attribute key
	 * @param value the attribute value to be attached
	 */
	void setAttribute(String name, @Nullable Object value);

	/**
	 * Get the value of the attribute identified by {@code name}.
	 * Return {@code null} if the attribute doesn't exist.
	 *
	 * 获取由名称标识的的属性值。
	 *
	 * @param name the unique attribute key
	 * @return the current value of the attribute, if any
	 */
	@Nullable
	Object getAttribute(String name);

	/**
	 * Remove the attribute identified by {@code name} and return its value.
	 * Return {@code null} if no attribute under {@code name} is found.
	 *
	 * 删除由名称标识的属性名并返回其值。如果不存在给定名称的属性，则返回null。
	 *
	 * @param name the unique attribute key 唯一属性键
	 * @return the last value of the attribute, if any 如果存在，返回属性的最后一个值
	 */
	@Nullable
	Object removeAttribute(String name);

	/**
	 * Return {@code true} if the attribute identified by {@code name} exists.
	 * Otherwise return {@code false}.
	 *
	 * 如果由名称标识的属性存在，则返回true，否则返回false。
	 *
	 * @param name the unique attribute key
	 */
	boolean hasAttribute(String name);

	/**
	 * Return the names of all attributes.
	 * 返回所有的属性名称。
	 */
	String[] attributeNames();

}

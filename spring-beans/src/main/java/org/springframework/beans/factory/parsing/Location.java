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

package org.springframework.beans.factory.parsing;

import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Class that models an arbitrary location in a {@link Resource resource}.
 *
 * 为Resource中为任意位置建模的类。
 *
 * <p>Typically used to track the location of problematic or erroneous
 * metadata in XML configuration files. For example, a
 * {@link #getSource() source} location might be 'The bean defined on
 * line 76 of beans.properties has an invalid Class'; another source might
 * be the actual DOM Element from a parsed XML {@link org.w3c.dom.Document};
 * or the source object might simply be {@code null}.
 *
 * 通常用来追踪XML配置文件中有问题或错误元数据的位置。例如，源位置可能是'beans.properties中第76行定义的bean拥有无效的Class'；
 * 其它的源可能来自已解析的Document中的实际DOM元素；或源对象可能只是null。
 *
 * @author Rob Harrop
 * @since 2.0
 */
public class Location {

	private final Resource resource;

	@Nullable
	private final Object source;


	/**
	 * Create a new instance of the {@link Location} class.
	 *
	 * 创建一个Location类的新实例
	 *
	 * @param resource the resource with which this location is associated 与此位置相关的资源
	 */
	public Location(Resource resource) {
		this(resource, null);
	}

	/**
	 * Create a new instance of the {@link Location} class.
	 *
	 * 创建一个Location类的新实例
	 *
	 * @param resource the resource with which this location is associated 与此位置关联的资源
	 * @param source the actual location within the associated resource 在相关资源中的实际位置
	 * (may be {@code null})
	 */
	public Location(Resource resource, @Nullable Object source) {
		Assert.notNull(resource, "Resource must not be null");
		this.resource = resource;
		this.source = source;
	}


	/**
	 * Get the resource with which this location is associated.
	 * 获取此位置关联的资源
	 */
	public Resource getResource() {
		return this.resource;
	}

	/**
	 * Get the actual location within the associated {@link #getResource() resource}
	 * (may be {@code null}).
	 *
	 * 获取关联的资源（可能为空）中的实际位置。
	 *
	 * <p>See the {@link Location class level javadoc for this class} for examples
	 * of what the actual type of the returned object may be.
	 *
	 * 返回对象的实际类型是什么的示例，参见此类的类级别javadoc。
	 *
	 */
	@Nullable
	public Object getSource() {
		return this.source;
	}

}

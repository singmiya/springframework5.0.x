/*
 * Copyright 2002-2012 the original author or authors.
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

package org.springframework.beans.factory;

import org.springframework.lang.Nullable;

/**
 * Sub-interface implemented by bean factories that can be part
 * of a hierarchy.
 *
 * 由bean工厂(factories)实现的子接口，他可能是层次结构的一部分。
 *
 * <p>The corresponding {@code setParentBeanFactory} method for bean
 * factories that allow setting the parent in a configurable
 * fashion can be found in the ConfigurableBeanFactory interface.
 *
 * 以配置的方式为bean工厂(factories)设置父bean工厂的相对应方法---setParentBeanFactory，可以在ConfigurableBeanFactory接口中找到。
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 07.07.2003
 * @see org.springframework.beans.factory.config.ConfigurableBeanFactory#setParentBeanFactory
 */
public interface HierarchicalBeanFactory extends BeanFactory {

	/**
	 * Return the parent bean factory, or {@code null} if there is none.
	 *
	 * 返回父bean工厂（factory）,如果不存在则返回null。
	 *
	 */
	@Nullable
	BeanFactory getParentBeanFactory();

	/**
	 * Return whether the local bean factory contains a bean of the given name,
	 * ignoring beans defined in ancestor contexts.
	 * <p>This is an alternative to {@code containsBean}, ignoring a bean
	 * of the given name from an ancestor bean factory.
	 *
	 * 确定给定名称的bean是否存在于本地bean工厂(factory)，忽略祖先（ancestor）上下文中定义的beans。
	 *
	 * @param name the name of the bean to query 要查询的bean名称
	 * @return whether a bean with the given name is defined in the local factory 本地工厂中是否定义了给定名称的bean
	 * @see BeanFactory#containsBean
	 */
	boolean containsLocalBean(String name);

}

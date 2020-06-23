/*
 * Copyright 2002-2007 the original author or authors.
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

import java.util.EventListener;

/**
 * Interface that receives callbacks for component, alias and import
 * registrations during a bean definition reading process.
 *
 * 在bean定义读取处理过程中，接收组件、别名和import注册的回调的接口。
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 2.0
 * @see ReaderContext
 */
public interface ReaderEventListener extends EventListener {

	/**
	 * Notification that the given defaults has been registered.
	 *
	 * 通知已注册给定的默认值。
	 *
	 * @param defaultsDefinition a descriptor for the defaults
	 * @see org.springframework.beans.factory.xml.DocumentDefaultsDefinition
	 */
	void defaultsRegistered(DefaultsDefinition defaultsDefinition);

	/**
	 * Notification that the given component has been registered.
	 *
	 * 通知已注册给定的组件。
	 *
	 * @param componentDefinition a descriptor for the new component
	 * @see BeanComponentDefinition
	 */
	void componentRegistered(ComponentDefinition componentDefinition);

	/**
	 * Notification that the given alias has been registered.
	 *
	 * 通知已注册给定别名。
	 *
	 * @param aliasDefinition a descriptor for the new alias
	 */
	void aliasRegistered(AliasDefinition aliasDefinition);

	/**
	 * Notification that the given import has been processed.
	 *
	 * 通知已处理给定导入（import）。
	 *
	 * @param importDefinition a descriptor for the import
	 */
	void importProcessed(ImportDefinition importDefinition);

}

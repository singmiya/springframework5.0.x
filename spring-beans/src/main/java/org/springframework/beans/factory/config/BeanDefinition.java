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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;
import org.springframework.lang.Nullable;

/**
 * A BeanDefinition describes a bean instance, which has property values,
 * constructor argument values, and further information supplied by
 * concrete implementations.
 *
 * BeanDefinition描述了一个bean实例，此实例具有属性值，构造参数值以及具体实现提供的更多信息。
 *
 * <p>This is just a minimal interface: The main intention is to allow a
 * {@link BeanFactoryPostProcessor} such as {@link PropertyPlaceholderConfigurer}
 * to introspect and modify property values and other bean metadata.
 *
 * 这是仅是一个简化的接口：主要目的是允许注入PropertyPlaceholderConfigurer之类的BeanFactoryPostProcessor进行内部检查（introspect）并
 * 修改属性值和其他bean元数据。
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 19.03.2004
 * @see ConfigurableListableBeanFactory#getBeanDefinition
 * @see org.springframework.beans.factory.support.RootBeanDefinition
 * @see org.springframework.beans.factory.support.ChildBeanDefinition
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	/**
	 * Scope identifier for the standard singleton scope: "singleton".
	 * <p>Note that extended bean factories might support further scopes.
	 *
	 * 用于标准单例作用域的作用域标识："singleton"。
	 * 注意，扩展bean工厂可能支持更多作用域。
	 *
	 * @see #setScope
	 */
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * Scope identifier for the standard prototype scope: "prototype".
	 * <p>Note that extended bean factories might support further scopes.
	 *
	 * 用于标准原型作用域的作用域标识："prototype"。
	 * 注意，扩展bean工厂可能支持更多作用域。
	 *
	 * @see #setScope
	 */
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;


	/**
	 * Role hint indicating that a {@code BeanDefinition} is a major part
	 * of the application. Typically corresponds to a user-defined bean.
	 *
	 * 角色提示，只是BeanDefinition是应用的主要部分。通常对应于用户定义的bean。
	 *
	 */
	int ROLE_APPLICATION = 0;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is a supporting
	 * part of some larger configuration, typically an outer
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 * {@code SUPPORT} beans are considered important enough to be aware
	 * of when looking more closely at a particular
	 * {@link org.springframework.beans.factory.parsing.ComponentDefinition},
	 * but not when looking at the overall configuration of an application.
	 *
	 * 角色提示，指示BeanDefinition是某些较大配置的支持部分，通常是外部ComponentDefinition。
	 * 当更仔细的查看特定的ComponentDefinition时，SUPPORT bean被认为是足够重要，足以引起注意，
	 * 但是在查看应用的整体配置时却没有。
	 *
	 */
	int ROLE_SUPPORT = 1;

	/**
	 * Role hint indicating that a {@code BeanDefinition} is providing an
	 * entirely background role and has no relevance to the end-user. This hint is
	 * used when registering beans that are completely part of the internal workings
	 * of a {@link org.springframework.beans.factory.parsing.ComponentDefinition}.
	 *
	 * 角色提示，指示BeanDefinition提供了完全的后台角色，与最终用户无关。
	 * 当注册完全属于ComponentDefinition内部工作一部分的bean时，使用此提示。
	 *
	 */
	int ROLE_INFRASTRUCTURE = 2;


	// Modifiable attributes

	/**
	 * Set the name of the parent definition of this bean definition, if any.
	 * 如果存在，设置此bean定义的父定义名称。
	 */
	void setParentName(@Nullable String parentName);

	/**
	 * Return the name of the parent definition of this bean definition, if any.
	 * 如果存在，返回此bean定义的父定义名称。
	 */
	@Nullable
	String getParentName();

	/**
	 * Specify the bean class name of this bean definition.
	 *
	 * 为此bean定义指定bean类名。
	 *
	 * <p>The class name can be modified during bean factory post-processing,
	 * typically replacing the original class name with a parsed variant of it.
	 *
	 * 在bean工厂执行后置处理期间，可以修改类名，通常，使用bean类名解析的变体替换原始类名。
	 *
	 * @see #setParentName
	 * @see #setFactoryBeanName
	 * @see #setFactoryMethodName
	 */
	void setBeanClassName(@Nullable String beanClassName);

	/**
	 * Return the current bean class name of this bean definition.
	 *
	 * 返回此bean定义的当前bean类名。
	 *
	 * <p>Note that this does not have to be the actual class name used at runtime, in
	 * case of a child definition overriding/inheriting the class name from its parent.
	 * Also, this may just be the class that a factory method is called on, or it may
	 * even be empty in case of a factory bean reference that a method is called on.
	 * Hence, do <i>not</i> consider this to be the definitive bean type at runtime but
	 * rather only use it for parsing purposes at the individual bean definition level.
	 *
	 * 注意，如果子定义从其父类继承了该类名的情况下，不必是运行时使用的实际类名。同样，这可能只是工厂方法调用的类，
	 * 或者，在方法调用工厂bean引用的情况下，甚至可能为空。因此，在运行时不要将其视为确定的bean类型，而仅在单个bean定义层次将其用于解析目的。
	 *
	 * @see #getParentName()
	 * @see #getFactoryBeanName()
	 * @see #getFactoryMethodName()
	 */
	@Nullable
	String getBeanClassName();

	/**
	 * Override the target scope of this bean, specifying a new scope name.
	 * 重写该bean的目标作用域，并为其设置新作用域名称。
	 * @see #SCOPE_SINGLETON
	 * @see #SCOPE_PROTOTYPE
	 */
	void setScope(@Nullable String scope);

	/**
	 * Return the name of the current target scope for this bean,
	 * 返回该bean当前目标作用域的名称。
	 * or {@code null} if not known yet.
	 */
	@Nullable
	String getScope();

	/**
	 * Set whether this bean should be lazily initialized.
	 *
	 * 设置该bean是否应该延迟加载。
	 *
	 * <p>If {@code false}, the bean will get instantiated on startup by bean
	 * factories that perform eager initialization of singletons.
	 *
	 * 如果设置false，则将在执行单例饥饿初始化的bean工厂启动时初始化bean。
	 *
	 */
	void setLazyInit(boolean lazyInit);

	/**
	 * Return whether this bean should be lazily initialized, i.e. not
	 * eagerly instantiated on startup. Only applicable to a singleton bean.
	 *
	 * 返回该bean是否应该延迟加载，即，启动时不急于式实例化。仅适用于单例bean。
	 *
	 */
	boolean isLazyInit();

	/**
	 * Set the names of the beans that this bean depends on being initialized.
	 * The bean factory will guarantee that these beans get initialized first.
	 *
	 * 设置此bean依赖于初始化的bean名称。bean工厂保证这些bean将首先被初始化。
	 *
	 */
	void setDependsOn(@Nullable String... dependsOn);

	/**
	 * Return the bean names that this bean depends on.
	 * 返回此bean依赖的bean名称。
	 */
	@Nullable
	String[] getDependsOn();

	/**
	 * Set whether this bean is a candidate for getting autowired into some other bean.
	 *
	 * 设置此bean是否适合自动装配到其他bean。
	 *
	 * <p>Note that this flag is designed to only affect type-based autowiring.
	 * It does not affect explicit references by name, which will get resolved even
	 * if the specified bean is not marked as an autowire candidate. As a consequence,
	 * autowiring by name will nevertheless inject a bean if the name matches.
	 *
	 * 注意，此标识旨在仅影响基于类型的自动装配。它不不会根据名称影响显示引用，尽管指定的bean未标记为自动装配候选项，名称也将得到解析。
	 * 结果，根据名称匹配，按照名称自动装配仍然会注入bean。
	 *
	 */
	void setAutowireCandidate(boolean autowireCandidate);

	/**
	 * Return whether this bean is a candidate for getting autowired into some other bean.
	 * 返回此bean是否适合自动装配到其他bean。
	 */
	boolean isAutowireCandidate();

	/**
	 * Set whether this bean is a primary autowire candidate.
	 *
	 * 设置此bean是否是主要的自动装配候选项。
	 *
	 * <p>If this value is {@code true} for exactly one bean among multiple
	 * matching candidates, it will serve as a tie-breaker.
	 *
	 * 如果对于多个匹配的候选项之间恰好有一个bean的值是true，它将作为决定项。
	 *
	 */
	void setPrimary(boolean primary);

	/**
	 * Return whether this bean is a primary autowire candidate.
	 * 返回此bean是否是主要的自动装配候选项。
	 */
	boolean isPrimary();

	/**
	 * Specify the factory bean to use, if any.
	 * This the name of the bean to call the specified factory method on.
	 *
	 * 如果存在，指定要使用的工厂bean。
	 * 这是调用指定工厂方法的bean的名称。
	 *
	 * @see #setFactoryMethodName
	 */
	void setFactoryBeanName(@Nullable String factoryBeanName);

	/**
	 * Return the factory bean name, if any.
	 * 如果存在，返回工厂bean名称。
	 */
	@Nullable
	String getFactoryBeanName();

	/**
	 * Specify a factory method, if any. This method will be invoked with
	 * constructor arguments, or with no arguments if none are specified.
	 * The method will be invoked on the specified factory bean, if any,
	 * or otherwise as a static method on the local bean class.
	 *
	 * 如果存在，指定工厂方法。将使用构造函数参数调用此方法，如果未指定，则不使用任何参数。
	 * 将在指定的工厂bean（如果存在）上调用该方法，否则将作为本地bean类上的静态方法被调用。
	 *
	 * @see #setFactoryBeanName
	 * @see #setBeanClassName
	 */
	void setFactoryMethodName(@Nullable String factoryMethodName);

	/**
	 * Return a factory method, if any.
	 * 如果存在，返回bean方法。
	 */
	@Nullable
	String getFactoryMethodName();

	/**
	 * Return the constructor argument values for this bean.
	 *
	 * 返回此bean的构造函数参数值。
	 *
	 * <p>The returned instance can be modified during bean factory post-processing.
	 *
	 * 返回的实例可以在bean工厂后置处理（post-processing）期间修改。
	 *
	 * @return the ConstructorArgumentValues object (never {@code null})
	 */
	ConstructorArgumentValues getConstructorArgumentValues();

	/**
	 * Return if there are constructor argument values defined for this bean.
	 * 返回此bean是否存在定义的构造函数参数值。
	 * @since 5.0.2
	 */
	default boolean hasConstructorArgumentValues() {
		return !getConstructorArgumentValues().isEmpty();
	}

	/**
	 * Return the property values to be applied to a new instance of the bean.
	 *
	 * 返回要应用于新bean实例的属性值。
	 *
	 * <p>The returned instance can be modified during bean factory post-processing.
	 *
	 * 返回的实例可以在bean工厂后置处理（post-processing）期间修改。
	 *
	 * @return the MutablePropertyValues object (never {@code null})
	 */
	MutablePropertyValues getPropertyValues();

	/**
	 * Return if there are property values values defined for this bean.
	 * 返回此bean是否存在定义的属性值。
	 * @since 5.0.2
	 */
	default boolean hasPropertyValues() {
		return !getPropertyValues().isEmpty();
	}


	// Read-only attributes

	/**
	 * Return whether this a <b>Singleton</b>, with a single, shared instance
	 * returned on all calls.
	 *
	 * 此Singleton是否在所有调用都会返回单个共享的实例。
	 *
	 * @see #SCOPE_SINGLETON
	 */
	boolean isSingleton();

	/**
	 * Return whether this a <b>Prototype</b>, with an independent instance
	 * returned for each call.
	 * 此Prototype是否在每次调用都会返回一个独立的实例。
	 * @since 3.0
	 * @see #SCOPE_PROTOTYPE
	 */
	boolean isPrototype();

	/**
	 * Return whether this bean is "abstract", that is, not meant to be instantiated.
	 * 返回此bean是否是"abstract"，即不可实例化。
	 */
	boolean isAbstract();

	/**
	 * Get the role hint for this {@code BeanDefinition}. The role hint
	 * provides the frameworks as well as tools with an indication of
	 * the role and importance of a particular {@code BeanDefinition}.
	 *
	 * 获取此BeanDefinition的角色提示。角色提示提供框架和工具，用来指示特定BeanDefinition的角色和重要性。
	 *
	 * @see #ROLE_APPLICATION
	 * @see #ROLE_SUPPORT
	 * @see #ROLE_INFRASTRUCTURE
	 */
	int getRole();

	/**
	 * Return a human-readable description of this bean definition.
	 * 返回此bean定义的可读描述
	 */
	@Nullable
	String getDescription();

	/**
	 * Return a description of the resource that this bean definition
	 * came from (for the purpose of showing context in case of errors).
	 * 返回此bean定义来源的资源的描述（在出现错误的情况下，用于展示上下文）。
	 */
	@Nullable
	String getResourceDescription();

	/**
	 * Return the originating BeanDefinition, or {@code null} if none.
	 * Allows for retrieving the decorated bean definition, if any.
	 *
	 * 返回原始的BeanDefinition，如果为空返回null。如果存在，允许检索装饰的bean定义。
	 *
	 * <p>Note that this method returns the immediate originator. Iterate through the
	 * originator chain to find the original BeanDefinition as defined by the user.
	 *
	 * 注意，此方法返回直接originator。通过遍历originator链来查找由用户定义的原始BeanDefinition。
	 *
	 */
	@Nullable
	BeanDefinition getOriginatingBeanDefinition();

}

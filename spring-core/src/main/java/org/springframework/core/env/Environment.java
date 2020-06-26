/*
 * Copyright 2002-2016 the original author or authors.
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

package org.springframework.core.env;

/**
 * Interface representing the environment in which the current application is running.
 * Models two key aspects of the application environment: <em>profiles</em> and
 * <em>properties</em>. Methods related to property access are exposed via the
 * {@link PropertyResolver} superinterface.
 *
 * 接口表示当前应用正在运行的环境。为应用环境的两个关键方面建模：profiles和properties。
 * 通过PropertyResolver超接口公开与属性访问相关的方法。
 *
 * <p>A <em>profile</em> is a named, logical group of bean definitions to be registered
 * with the container only if the given profile is <em>active</em>. Beans may be assigned
 * to a profile whether defined in XML or via annotations; see the spring-beans 3.1 schema
 * or the {@link org.springframework.context.annotation.Profile @Profile} annotation for
 * syntax details. The role of the {@code Environment} object with relation to profiles is
 * in determining which profiles (if any) are currently {@linkplain #getActiveProfiles
 * active}, and which profiles (if any) should be {@linkplain #getDefaultProfiles active
 * by default}.
 *
 * 对于名称为profile的逻辑bean定义组，只有在profile处于激活状态时才能被注册到容器中。
 * 可以将bean赋值给profile，不管是在XML中定义还是通过注解定义。具体语法参见spring-beans 3.1模式或@Profile注解。
 * 与profiles相关的Environment对象的作用是，确定哪个profiles（如果存在）当前正处于激活状态，确定哪个profiles（如果存在）应该是默认激活状态。
 *
 * <p><em>Properties</em> play an important role in almost all applications, and may
 * originate from a variety of sources: properties files, JVM system properties, system
 * environment variables, JNDI, servlet context parameters, ad-hoc Properties objects,
 * Maps, and so on. The role of the environment object with relation to properties is to
 * provide the user with a convenient service interface for configuring property sources
 * and resolving properties from them.
 *
 * Properties几乎在所有应用中都起着重要作用，并且可能来自多种源：properties文件，JVM系统属性，系统环境变量，JNDI，servlet上下文参数，
 * ad-doc Properties对象，Maps等等。与属性相关的环境对象的作用是为用户提供便利服务接口，以用来配置属性源并从中解析属性。
 *
 * <p>Beans managed within an {@code ApplicationContext} may register to be {@link
 * org.springframework.context.EnvironmentAware EnvironmentAware} or {@code @Inject} the
 * {@code Environment} in order to query profile state or resolve properties directly.
 *
 * 在ApplicationContext中管理的bean可以注册为EnvironmentAware或使用@Inject注入Environment，以便于查询profile状态或直接解析properties。
 *
 * <p>In most cases, however, application-level beans should not need to interact with the
 * {@code Environment} directly but instead may have to have {@code ${...}} property
 * values replaced by a property placeholder configurer such as
 * {@link org.springframework.context.support.PropertySourcesPlaceholderConfigurer
 * PropertySourcesPlaceholderConfigurer}, which itself is {@code EnvironmentAware} and
 * as of Spring 3.1 is registered by default when using
 * {@code <context:property-placeholder/>}.
 *
 * 然而，在多数情况下，应用级别的bean不需要直接与Environment交互，
 * 而是需要用诸如PropertySourcesPlaceholderConfigurer这样的属性占位符配置器将${...}属性值替换掉，
 * 从Spring 3.1开始，当使用<context:property-placeholder/>时，配置器它自身被默认注册为EnvironmentAware。
 *
 * <p>Configuration of the environment object must be done through the
 * {@code ConfigurableEnvironment} interface, returned from all
 * {@code AbstractApplicationContext} subclass {@code getEnvironment()} methods. See
 * {@link ConfigurableEnvironment} Javadoc for usage examples demonstrating manipulation
 * of property sources prior to application context {@code refresh()}.
 *
 * 通过ConfigurableEnvironment接口，必须完成环境（environment）对象的配置，并通过AbstractApplicationContext子类的getEnvironment()方法返回。
 * 在应用上下文refresh()之前对属性源操作的用例展示参见ConfigurableEnvironment java文档。
 *
 * @author Chris Beams
 * @since 3.1
 * @see PropertyResolver
 * @see EnvironmentCapable
 * @see ConfigurableEnvironment
 * @see AbstractEnvironment
 * @see StandardEnvironment
 * @see org.springframework.context.EnvironmentAware
 * @see org.springframework.context.ConfigurableApplicationContext#getEnvironment
 * @see org.springframework.context.ConfigurableApplicationContext#setEnvironment
 * @see org.springframework.context.support.AbstractApplicationContext#createEnvironment
 */
public interface Environment extends PropertyResolver {

	/**
	 * Return the set of profiles explicitly made active for this environment. Profiles
	 * are used for creating logical groupings of bean definitions to be registered
	 * conditionally, for example based on deployment environment.  Profiles can be
	 * activated by setting {@linkplain AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
	 * "spring.profiles.active"} as a system property or by calling
	 * {@link ConfigurableEnvironment#setActiveProfiles(String...)}.
	 * <p>If no profiles have explicitly been specified as active, then any
	 * {@linkplain #getDefaultProfiles() default profiles} will automatically be activated.
	 *
	 * 返回次环境中显示激活的profiles集合。Profiles用于创建有条件注册的bean定义逻辑分组，例如基于部署环境。
	 * Profiles可以通过设置"spring.profiles.active"系统属性或调用ConfigurableEnvironment.setActiveProfiles(String...)方法进行激活。
	 * 如果没有显示指定激活的profiles，那么将自动激活任何默认的profiles。
	 *
	 * @see #getDefaultProfiles
	 * @see ConfigurableEnvironment#setActiveProfiles
	 * @see AbstractEnvironment#ACTIVE_PROFILES_PROPERTY_NAME
	 */
	String[] getActiveProfiles();

	/**
	 * Return the set of profiles to be active by default when no active profiles have
	 * been set explicitly.
	 *
	 * 当未显示激活profiles时，默认返回一组profiles作为激活状态的profiles。
	 *
	 * @see #getActiveProfiles
	 * @see ConfigurableEnvironment#setDefaultProfiles
	 * @see AbstractEnvironment#DEFAULT_PROFILES_PROPERTY_NAME
	 */
	String[] getDefaultProfiles();

	/**
	 * Return whether one or more of the given profiles is active or, in the case of no
	 * explicit active profiles, whether one or more of the given profiles is included in
	 * the set of default profiles. If a profile begins with '!' the logic is inverted,
	 * i.e. the method will return true if the given profile is <em>not</em> active.
	 * For example, <pre class="code">env.acceptsProfiles("p1", "!p2")</pre> will
	 * return {@code true} if profile 'p1' is active or 'p2' is not active.
	 * @throws IllegalArgumentException if called with zero arguments
	 * or if any profile is {@code null}, empty or whitespace-only
	 *
	 * 返回是否有一个或多个profiles处于激活状态，如果没有显示激活profiles，给定的一个或多个profiles是否包含在默认profiles集合中。
	 * 如果profile以"!"开头，那就会与正确逻辑相反，即，如果给定的profile未激活则会返回true。
	 * 例如，如果'p1'处于激活状态或'p2'未激活，那么env.acceptsProfiles("p1", "!p2")就会返回true。
	 *
	 * @see #getActiveProfiles
	 * @see #getDefaultProfiles
	 */
	boolean acceptsProfiles(String... profiles);

}

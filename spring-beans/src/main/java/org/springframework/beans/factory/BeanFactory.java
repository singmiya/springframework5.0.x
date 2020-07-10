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

package org.springframework.beans.factory;

import org.springframework.beans.BeansException;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;

/**
 * The root interface for accessing a Spring bean container.
 * This is the basic client view of a bean container;
 * further interfaces such as {@link ListableBeanFactory} and
 * {@link org.springframework.beans.factory.config.ConfigurableBeanFactory}
 * are available for specific purposes.
 *
 * 用于访问Spring bean 容器的根接口。
 * 这是一个bean容器的基础客户端视图；诸如ListableBeanFactory和ConfigurableBeanFactory的其他接口可用于其他特定目的。
 *
 *
 * <p>This interface is implemented by objects that hold a number of bean definitions,
 * each uniquely identified by a String name. Depending on the bean definition,
 * the factory will return either an independent instance of a contained object
 * (the Prototype design pattern), or a single shared instance (a superior
 * alternative to the Singleton design pattern, in which the instance is a
 * singleton in the scope of the factory). Which type of instance will be returned
 * depends on the bean factory configuration: the API is the same. Since Spring
 * 2.0, further scopes are available depending on the concrete application
 * context (e.g. "request" and "session" scopes in a web environment).
 *
 * 该接口由包含多个bean定义的对象实现，每个bean定义用一个字符串名称唯一标识。根据bean的定义，
 * factory将会返回其包含对象的单独实例（原型（Prototype）设计模式），或一个单个共享实例（单例（Singleton）模式的更好替代品，
 * 其中实例是在整个factory范围内的单例）。返回那种实例有bean factory配置决定：API都是同一个。
 * 自Spring2.0开始，根据具体的上下文，可以提供更多的范围（例如：在网络环境中的"request"和"session"范围）。
 *
 * <p>The point of this approach is that the BeanFactory is a central registry
 * of application components, and centralizes configuration of application
 * components (no more do individual objects need to read properties files,
 * for example). See chapters 4 and 11 of "Expert One-on-One J2EE Design and
 * Development" for a discussion of the benefits of this approach.
 *
 * 这种方式的重点是，BeanFactory是应用组件的中央注册表，并集中了应用组件的配置（例如：不需要在对单个对象读取配置文件）。
 * 对使用这种方式好处的讨论，请参看"Expert One-on-One J2EE Design and Development"的第4章和第11章。
 *
 * <p>Note that it is generally better to rely on Dependency Injection
 * ("push" configuration) to configure application objects through setters
 * or constructors, rather than use any form of "pull" configuration like a
 * BeanFactory lookup. Spring's Dependency Injection functionality is
 * implemented using this BeanFactory interface and its subinterfaces.
 *
 * 请注意，最好依靠依赖注入（Dependency Injection）（"push" 配置）通过setters或constructors去配置应用对象，
 * 而不是使用任何形式的"pull"方式去配置应用对象，像：BeanFactory查找。
 * 使用此BeanFactory和其子接口可以实现Spring的依赖注入（Dependency Injection）功能。
 *
 * <p>Normally a BeanFactory will load bean definitions stored in a configuration
 * source (such as an XML document), and use the {@code org.springframework.beans}
 * package to configure the beans. However, an implementation could simply return
 * Java objects it creates as necessary directly in Java code. There are no
 * constraints on how the definitions could be stored: LDAP, RDBMS, XML,
 * properties file, etc. Implementations are encouraged to support references
 * amongst beans (Dependency Injection).
 *
 * 通常情况下，BeanFactory将会从存储在配置资源中加载bean定义（如：XML文档），并使用org.springframework.beans包配置beans。
 * 然而，实现可以简单地返回根据需要直接在Java代码中创建的Java对象。对于（bean）定义如何存储并此处没有约束：LDAP、RDBMS、XML、属性文件等等。
 * 鼓励在（接口）实现中支持beans之间的引用（依赖注入）。
 *
 * <p>In contrast to the methods in {@link ListableBeanFactory}, all of the
 * operations in this interface will also check parent factories if this is a
 * {@link HierarchicalBeanFactory}. If a bean is not found in this factory instance,
 * the immediate parent factory will be asked. Beans in this factory instance
 * are supposed to override beans of the same name in any parent factory.
 *
 * 与ListableBeanFactory中的方法相反，如果这是一个HierarchicalBeanFactory，则此接口中的所有操作还将会检查父factories。
 * 如果在此factory实例中未找到bean，则从其直接父factory中查找。此factory实例中的beans应该覆盖在任何父factory中的同名beans。
 *
 * <p>Bean factory implementations should support the standard bean lifecycle interfaces
 * as far as possible. The full set of initialization methods and their standard order is:
 *
 * ！！！！！！
 * Bean factory 实现应尽可能支持标准bean生命周期接口。全套的初始化方法及其标准顺序为：
 * ！！！！！！
 *
 * <ol>
 * <li>BeanNameAware's {@code setBeanName}
 * <li>BeanClassLoaderAware's {@code setBeanClassLoader}
 * <li>BeanFactoryAware's {@code setBeanFactory}
 * <li>EnvironmentAware's {@code setEnvironment}
 * <li>EmbeddedValueResolverAware's {@code setEmbeddedValueResolver}
 * <li>ResourceLoaderAware's {@code setResourceLoader}
 * (only applicable when running in an application context) 仅在应用上下文运行时使用
 * <li>ApplicationEventPublisherAware's {@code setApplicationEventPublisher}
 * (only applicable when running in an application context)
 * <li>MessageSourceAware's {@code setMessageSource}
 * (only applicable when running in an application context)
 * <li>ApplicationContextAware's {@code setApplicationContext}
 * (only applicable when running in an application context)
 * <li>ServletContextAware's {@code setServletContext}
 * (only applicable when running in a web application context)
 * <li>{@code postProcessBeforeInitialization} methods of BeanPostProcessors
 * <li>InitializingBean's {@code afterPropertiesSet}
 * <li>a custom init-method definition
 * <li>{@code postProcessAfterInitialization} methods of BeanPostProcessors
 * </ol>
 *
 * <p>On shutdown of a bean factory, the following lifecycle methods apply:
 *
 * 当销毁bean factory时，执行以下生命周期方法：
 *
 * <ol>
 * <li>{@code postProcessBeforeDestruction} methods of DestructionAwareBeanPostProcessors
 * <li>DisposableBean's {@code destroy}
 * <li>a custom destroy-method definition
 * </ol>
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Chris Beams
 * @since 13 April 2001
 * @see BeanNameAware#setBeanName
 * @see BeanClassLoaderAware#setBeanClassLoader
 * @see BeanFactoryAware#setBeanFactory
 * @see org.springframework.context.ResourceLoaderAware#setResourceLoader
 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher
 * @see org.springframework.context.MessageSourceAware#setMessageSource
 * @see org.springframework.context.ApplicationContextAware#setApplicationContext
 * @see org.springframework.web.context.ServletContextAware#setServletContext
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization
 * @see InitializingBean#afterPropertiesSet
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getInitMethodName
 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization
 * @see DisposableBean#destroy
 * @see org.springframework.beans.factory.support.RootBeanDefinition#getDestroyMethodName
 */
public interface BeanFactory {

	/**
	 * Used to dereference a {@link FactoryBean} instance and distinguish it from
	 * beans <i>created</i> by the FactoryBean. For example, if the bean named
	 * {@code myJndiObject} is a FactoryBean, getting {@code &myJndiObject}
	 * will return the factory, not the instance returned by the factory.
	 *
	 * 用于FactoryBean实例"解引用"，其区别于由FactoryBean创建的beans。
	 * 例如：如果名为myJndiObject的bean类型为FactoryBean，获取 &myJndiObject将会返回一个factory，而不是由factory返回的（其他类型bean）实例。
	 *
	 *
	 * 注："解引用"：在Java中，return null 是否安全， 为什么？ - RednaxelaFX的回答 - 知乎
	 * https://www.zhihu.com/question/47997295/answer/108624397
	 *
	 */
	String FACTORY_BEAN_PREFIX = "&";


	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>This method allows a Spring BeanFactory to be used as a replacement for the
	 * Singleton or Prototype design pattern. Callers may retain references to
	 * returned objects in the case of Singleton beans.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 *
	 * 返回一个是指定bean的共享或者独立实例。这个方法允许使用Spring BeanFactory来替代单例（Singleton）或原型（Prototype）设计模式。
	 * 就单例而言，调用者会持有该方法返回对象的引用。
	 *
	 * 将别名转换为相应的标准bean名称。如果未从此factory实例中找到bean，将会继续从父factory中寻找。
	 *
	 * @param name the name of the bean to retrieve 将要检索的bean的名称
	 * @return an instance of the bean bean实例
	 * @throws NoSuchBeanDefinitionException if there is no bean with the specified name 指定名称的bean不存在
	 * @throws BeansException if the bean could not be obtained 无法获取bean
	 */
	Object getBean(String name) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Behaves the same as {@link #getBean(String)}, but provides a measure of type
	 * safety by throwing a BeanNotOfRequiredTypeException if the bean is not of the
	 * required type. This means that ClassCastException can't be thrown on casting
	 * the result correctly, as can happen with {@link #getBean(String)}.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 *
	 * 返回一个指定bean的共享或者单独实例。具体行为与#getBean(String)相同，但，如果bean类型不是所需的类型时，
	 * 则通过抛出 BeanNotOfRequiredTypeException异常来提供类型安全措施。
	 * 这意味着，像#getBean(String)方法中那样的，在无法正确的进行类型转换时抛出ClassCastException将不会发生。
	 *
	 * 将别名转换为相应的标准bean名称。如果为从此factory实例中找到bean，将会继续从父factory中寻找。
	 *
	 * @param name the name of the bean to retrieve 将要检索的bean名称
	 * @param requiredType type the bean must match. Can be an interface or superclass
	 * of the actual class, or {@code null} for any match. For example, if the value
	 * is {@code Object.class}, this method will succeed whatever the class of the
	 * returned instance.
	 *
	 * bean必须匹配的类型。可以是实例类的接口或父类，或可以匹配任何类型的null。
	 * 例如，如果参数值为Object.class，则不管需要返回的实例是何种类型此方法都会成功。
	 *
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition bean定义不存在
	 * @throws BeanNotOfRequiredTypeException if the bean is not of the required type 如果bean不是所需类型
	 * @throws BeansException if the bean could not be created bean无法创建
	 */
	<T> T getBean(String name, @Nullable Class<T> requiredType) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Allows for specifying explicit constructor arguments / factory method arguments,
	 * overriding the specified default arguments (if any) in the bean definition.
	 *
	 * 返回一个指定bean的共享或单独实例。允许指定显示构造函数和工厂（factory）方法中的参数，来覆盖bean定义中指定的默认参数（如果有）。
	 *
	 * @param name the name of the bean to retrieve 要检索的bean名称
	 * @param args arguments to use when creating a bean instance using explicit arguments
	 * (only applied when creating a new instance as opposed to retrieving an existing one)
	 *
	 * 使用显示参数构建bean实例时所需的参数（仅在创建新实例而不是检索已存在实例时使用）
	 *
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanDefinitionStoreException if arguments have been given but
	 * the affected bean isn't a prototype 参数已提供，但受影响的bean不是原型（prototype）类型的
	 * @throws BeansException if the bean could not be created
	 * @since 2.5
	 */
	Object getBean(String name, Object... args) throws BeansException;

	/**
	 * Return the bean instance that uniquely matches the given object type, if any.
	 * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
	 * but may also be translated into a conventional by-name lookup based on the name
	 * of the given type. For more extensive retrieval operations across sets of beans,
	 * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
	 *
	 * 如果存在，则返回与所给对象类型唯一匹配的bean实例。此方法会进入ListableBeanFactory类按类型查找区域，但也可以根据所给类型的名称转换为常规的按名称查找（方式）。
	 * 若想对beans集合进行更广泛的检索操作，请使用ListableBeanFactory和/或BeanFactoryUtils
	 *
	 * @param requiredType type the bean must match; can be an interface or superclass 必须匹配的bean类型；可以是接口或者父类
	 * @return an instance of the single bean matching the required type 匹配所需类型的单个bean实例
	 * @throws NoSuchBeanDefinitionException if no bean of the given type was found
	 * @throws NoUniqueBeanDefinitionException if more than one bean of the given type was found 找到多个匹配给定类型的bean
	 * @throws BeansException if the bean could not be created
	 * @since 3.0
	 * @see ListableBeanFactory
	 */
	<T> T getBean(Class<T> requiredType) throws BeansException;

	/**
	 * Return an instance, which may be shared or independent, of the specified bean.
	 * <p>Allows for specifying explicit constructor arguments / factory method arguments,
	 * overriding the specified default arguments (if any) in the bean definition.
	 * <p>This method goes into {@link ListableBeanFactory} by-type lookup territory
	 * but may also be translated into a conventional by-name lookup based on the name
	 * of the given type. For more extensive retrieval operations across sets of beans,
	 * use {@link ListableBeanFactory} and/or {@link BeanFactoryUtils}.
	 *
	 * 返回一个指定bean的共享或单独实例。允许指定显示构造方法/工厂方法参数，来覆盖bean定义中指定的默认参数（如果有）。
	 * 这个方法会进入ListableBeanFactory按类型检索区域，但也可能根据给定类型的名称转换为按常规名称检索。
	 * 若想对beas集合进行更广泛的操作，请使用ListableBeanFactory或BeanFactoryUtils。
	 *
	 * @param requiredType type the bean must match; can be an interface or superclass bean必须匹配的类型；可以是接口或者父类
	 * @param args arguments to use when creating a bean instance using explicit arguments
	 * (only applied when creating a new instance as opposed to retrieving an existing one)
	 *
	 * 使用显示参数创建bean实例时所使用的参数（在创建新实例而不是检索已存在的时候才适用）
	 *
	 * @return an instance of the bean
	 * @throws NoSuchBeanDefinitionException if there is no such bean definition
	 * @throws BeanDefinitionStoreException if arguments have been given but
	 * the affected bean isn't a prototype 参数已提供，但受影响的bean不是原型（prototype）
	 * @throws BeansException if the bean could not be created
	 * @since 4.1
	 */
	<T> T getBean(Class<T> requiredType, Object... args) throws BeansException;


	/**
	 * Does this bean factory contain a bean definition or externally registered singleton
	 * instance with the given name?
	 * <p>If the given name is an alias, it will be translated back to the corresponding
	 * canonical bean name.
	 * <p>If this factory is hierarchical, will ask any parent factory if the bean cannot
	 * be found in this factory instance.
	 * <p>If a bean definition or singleton instance matching the given name is found,
	 * this method will return {@code true} whether the named bean definition is concrete
	 * or abstract, lazy or eager, in scope or not. Therefore, note that a {@code true}
	 * return value from this method does not necessarily indicate that {@link #getBean}
	 * will be able to obtain an instance for the same name.
	 *
	 * bean工厂(factory)中是否存在给定名称的bean定义或外部单例注册。
	 *
	 * 如果给定的名称是别名，方法将会将其转换为相应的规范bean名称。如果工厂(factory)是分层的，当无法在此工厂(factory)实例中检索到bean时，
	 * 将会去父工厂(factory)中继线检索。
	 *
	 * 如果检索到与此名称匹配的bean定义或单例，无论用此名称命名的bean定义是具体的还是抽象的，懒加载还是饥饿加载，在不在适用范围，此方法都会返回true。
	 * 因此，注意此方法返回true时，并不代表#getBean方法就会返回与此名称相同的实例。
	 *
	 * @param name the name of the bean to query 检索bean的名称
	 * @return whether a bean with the given name is present 给定名称的bean是否存在
	 */
	boolean containsBean(String name);

	/**
	 * Is this bean a shared singleton? That is, will {@link #getBean} always
	 * return the same instance?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * independent instances. It indicates non-singleton instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isPrototype} operation to explicitly
	 * check for independent instances.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 *
	 * 此名称对应的bean是否是单例？也就是说，#getBean是否总会返回同一个实例？
	 * 注意：即使此方法返回false也不能明确的表明它是一个单独实例。只能表明此bean并非单例，也可能是其他作用范围bean（prototype、session、request）。
	 * 使用#isPrototype操作来明确的检查（bean）是否是单独实例。
	 *
	 * 把别名转为相应的规范的bean名称。如果未在此工厂(factory)实例中检索到bean，此方法将继续在父工厂(factory)中检索。
	 *
	 *
	 * @param name the name of the bean to query
	 * @return whether this bean corresponds to a singleton instance 此bean是否是对应一个单例
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name 给定名称的bean不存在
	 * @see #getBean
	 * @see #isPrototype
	 */
	boolean isSingleton(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Is this bean a prototype? That is, will {@link #getBean} always return
	 * independent instances?
	 * <p>Note: This method returning {@code false} does not clearly indicate
	 * a singleton object. It indicates non-independent instances, which may correspond
	 * to a scoped bean as well. Use the {@link #isSingleton} operation to explicitly
	 * check for a shared singleton instance.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 *
	 * bean是否是原型(prototype)类型？也就是说，#getBean是否总是返回独立实例。
	 * 注意：如果此方法返回false也不能明确的表明它是一个单例对象。这只能表明它是一个非独立实例，也可能是其他作用范围bean。
	 * 使用#getSingleton操作来明确的检查（bean）是否是单例。
	 *
	 * 把别名转为相对应的规范bean名称。如果未在此工厂(factory)实例中检索到bean，此方法将继续在父类(factory)中检索。
	 *
	 * @param name the name of the bean to query
	 * @return whether this bean will always deliver independent instances
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.3
	 * @see #getBean
	 * @see #isSingleton
	 */
	boolean isPrototype(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Check whether the bean with the given name matches the specified type.
	 * More specifically, check whether a {@link #getBean} call for the given name
	 * would return an object that is assignable to the specified target type.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 *
	 * 检查具有给定名称的bean是否与指定的类型相匹配。更具体的来说，检查给定名称的#getBean调用是否会返回与指定目标类型相匹配的对象。
	 * 把别名转为相对应的规范bean名称。如果未在此工厂(factory)实例中检索到bean，此方法将继续在父类工厂(factory)中检索。
	 *
	 * @param name the name of the bean to query
	 * @param typeToMatch the type to match against (as a {@code ResolvableType}) 要匹配的类型（ResolvableType类型）
	 * @return {@code true} if the bean type matches,
	 * {@code false} if it doesn't match or cannot be determined yet
	 * 如果与bean类型相匹配返回true，如果不匹配或者无法确定，则返回false
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 4.2
	 * @see #getBean
	 * @see #getType
	 */
	boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;

	/**
	 * Check whether the bean with the given name matches the specified type.
	 * More specifically, check whether a {@link #getBean} call for the given name
	 * would return an object that is assignable to the specified target type.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 *
	 * 检查具有给定名称的bean是否与指定类型相匹配。更具体的讲，检查给定名称的#getBean调用是否会返回一个与指定目标类型相匹配的对象。
	 * 把别名转为相应的规范bean名称。如果未在此工厂（factory）实例中检索到bean，此方法将继续在父类工厂（factory）中检索。
	 *
	 * @param name the name of the bean to query 要检索的bean名称
	 * @param typeToMatch the type to match against (as a {@code Class}) 要匹配的类型（Class类型）
	 * @return {@code true} if the bean type matches,
	 * {@code false} if it doesn't match or cannot be determined yet
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.0.1
	 * @see #getBean
	 * @see #getType
	 */
	boolean isTypeMatch(String name, @Nullable Class<?> typeToMatch) throws NoSuchBeanDefinitionException;

	/**
	 * Determine the type of the bean with the given name. More specifically,
	 * determine the type of object that {@link #getBean} would return for the given name.
	 * <p>For a {@link FactoryBean}, return the type of object that the FactoryBean creates,
	 * as exposed by {@link FactoryBean#getObjectType()}.
	 * <p>Translates aliases back to the corresponding canonical bean name.
	 * Will ask the parent factory if the bean cannot be found in this factory instance.
	 *
	 * 确定给定名称bean的类型。更具体的讲，确定#getBean根据给定名称可能返回对象的类型。
	 * 对于FactoryBean，返回由Factory#getObjectType()公开的FactoryBean创建的对象类型。
	 * 把别名转为相应的规范bean名称。如果未在此工厂（factory）实例中检索到bean，此方法将继续在父类工厂（factory）中检索。
	 *
	 * @param name the name of the bean to query
	 * @return the type of the bean, or {@code null} if not determinable 返回bean类型，如果无法确定返回null
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name 给定名称的bean不存在
	 * @since 1.1.2
	 * @see #getBean
	 * @see #isTypeMatch
	 */
	@Nullable
	Class<?> getType(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Return the aliases for the given bean name, if any.
	 * All of those aliases point to the same bean when used in a {@link #getBean} call.
	 * <p>If the given name is an alias, the corresponding original bean name
	 * and other aliases (if any) will be returned, with the original bean name
	 * being the first element in the array.
	 * <p>Will ask the parent factory if the bean cannot be found in this factory instance.
	 *
	 * 如果存在的话，返回给定名称bean的别名。当使用#getBean调用时，所有的别名都指向相同的bean。
	 * 如果给定名称是别名，将会返回相应的原始bean名称和其他的别名（如果有），且原始bean名称是数组的第一个元素。
	 * 如果未在此工厂（factory）实例中检索到bean，此方法将继续在父类工厂（factory）中检索。
	 *
	 * @param name the bean name to check for aliases 用来检查别名的bean名称
	 * @return the aliases, or an empty array if none 别名，如果不存在，则返回空数组
	 * @see #getBean
	 */
	String[] getAliases(String name);

}

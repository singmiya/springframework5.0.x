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

import java.util.Iterator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.lang.Nullable;

/**
 * Configuration interface to be implemented by most listable bean factories.
 * In addition to {@link ConfigurableBeanFactory}, it provides facilities to
 * analyze and modify bean definitions, and to pre-instantiate singletons.
 *
 * 配置接口将大部分由可遍历bean工厂实现。除了ConfigurableBeanFactory，它还提供了用于分析、修改bean定义及预实例化单例的工具。
 *
 * <p>This subinterface of {@link org.springframework.beans.factory.BeanFactory}
 * is not meant to be used in normal application code: Stick to
 * {@link org.springframework.beans.factory.BeanFactory} or
 * {@link org.springframework.beans.factory.ListableBeanFactory} for typical
 * use cases. This interface is just meant to allow for framework-internal
 * plug'n'play even when needing access to bean factory configuration methods.
 *
 * BeanFactory的此子接口不打算将其用在常规应用代码中：对于典型用例，坚持使用BeanFactory和ListableBeanFactory来解决。
 *
 * @author Juergen Hoeller
 * @since 03.11.2003
 * @see org.springframework.context.support.AbstractApplicationContext#getBeanFactory()
 */
public interface ConfigurableListableBeanFactory
		extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

	/**
	 * Ignore the given dependency type for autowiring:
	 * for example, String. Default is none.
	 *
	 * 忽略对给定依赖类型进行自动装配：例如，String。默认为空。
	 *
	 * @param type the dependency type to ignore
	 */
	void ignoreDependencyType(Class<?> type);

	/**
	 * Ignore the given dependency interface for autowiring.
	 *
	 * 忽略对给定依赖接口进行自动装配。
	 *
	 * <p>This will typically be used by application contexts to register
	 * dependencies that are resolved in other ways, like BeanFactory through
	 * BeanFactoryAware or ApplicationContext through ApplicationContextAware.
	 *
	 * 此方法通常被应用上下文用来注册由其他方式解析的依赖关系，像，BeanFactory通过BeanFactoryAware或ApplicationContext通过ApplicationContextAware。
	 *
	 * <p>By default, only the BeanFactoryAware interface is ignored.
	 * For further types to ignore, invoke this method for each type.
	 *
	 * 默认情况下，仅会忽略BeanFactoryAware接口。如果要忽略其他类型，请为每一种类型调用此方法。
	 *
	 * @param ifc the dependency interface to ignore
	 * @see org.springframework.beans.factory.BeanFactoryAware
	 * @see org.springframework.context.ApplicationContextAware
	 */
	void ignoreDependencyInterface(Class<?> ifc);

	/**
	 * Register a special dependency type with corresponding autowired value.
	 *
	 * 用相应的自动装配值注册一个特殊的依赖类型。
	 *
	 * <p>This is intended for factory/context references that are supposed
	 * to be autowirable but are not defined as beans in the factory:
	 * e.g. a dependency of type ApplicationContext resolved to the
	 * ApplicationContext instance that the bean is living in.
	 *
	 * 此方法用于工厂/上下文引用，这些引用应该是可自动装配的，但不像bean一样定义在工厂中：
	 * 例如，ApplicationContext类型的依赖已解析为bean所在的ApplicationContext实例。
	 *
	 * <p>Note: There are no such default types registered in a plain BeanFactory,
	 * not even for the BeanFactory interface itself.
	 *
	 * 注意：在普通的BeanFactory中没有注册此类默认类型，甚至对于BeanFactory接口本身也没有。
	 *
	 * @param dependencyType the dependency type to register. This will typically
	 * be a base interface such as BeanFactory, with extensions of it resolved
	 * as well if declared as an autowiring dependency (e.g. ListableBeanFactory),
	 * as long as the given value actually implements the extended interface.
	 *                       用于注册的依赖类型。这通常是一个基础接口，例如BeanFactory，
	 *                       如果声明为自动装配依赖关系（例如，ListableBeanFactory），则也可以解析为它的扩展，
	 *                       只要给定值实际实现了此扩展接口即可。
	 * @param autowiredValue the corresponding autowired value. This may also be an
	 * implementation of the {@link org.springframework.beans.factory.ObjectFactory}
	 * interface, which allows for lazy resolution of the actual target value.
	 *                       相应的自动装配值。也可以是ObjectFactory接口的实现，它允许延迟解析实际的目标值。
	 */
	void registerResolvableDependency(Class<?> dependencyType, @Nullable Object autowiredValue);

	/**
	 * Determine whether the specified bean qualifies as an autowire candidate,
	 * to be injected into other beans which declare a dependency of matching type.
	 * <p>This method checks ancestor factories as well.
	 *
	 * 确实指定的bean是否符合自动装配候选条件，将其注入到与声明类型匹配的其他bean中。
	 * 此方法也会检查祖先工厂。
	 *
	 * @param beanName the name of the bean to check 要检查的bean名称
	 * @param descriptor the descriptor of the dependency to resolve 要解析的依赖关系描述符
	 * @return whether the bean should be considered as autowire candidate 是否可将bean视为自动装配候选对象
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name 不存在给定名称的bean
	 */
	boolean isAutowireCandidate(String beanName, DependencyDescriptor descriptor)
			throws NoSuchBeanDefinitionException;

	/**
	 * Return the registered BeanDefinition for the specified bean, allowing access
	 * to its property values and constructor argument value (which can be
	 * modified during bean factory post-processing).
	 *
	 * 返回指定bean的已注册BeanDefinition，允许访问它的属性值和构造参数值（可以在bean工厂后期处理期间修改这些值）。
	 *
	 * <p>A returned BeanDefinition object should not be a copy but the original
	 * definition object as registered in the factory. This means that it should
	 * be castable to a more specific implementation type, if necessary.
	 *
	 * 返回的BeanDefinition对象不能是一份拷贝，而是已注册在工厂中的原始定义对象。这意味着如有需要，它可以强制转换为更具体的实现类型。
	 *
	 * <p><b>NOTE:</b> This method does <i>not</i> consider ancestor factories.
	 * It is only meant for accessing local bean definitions of this factory.
	 *
	 * 注意：此方法不考虑祖先工厂。它只可以获取此工厂的本地bean定义。
	 *
	 * @param beanName the name of the bean
	 * @return the registered BeanDefinition
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * defined in this factory
	 */
	BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * Return a unified view over all bean names managed by this factory.
	 *
	 * 返回由此工厂管理的所有bean名称的统一视图。
	 *
	 * <p>Includes bean definition names as well as names of manually registered
	 * singleton instances, with bean definition names consistently coming first,
	 * analogous to how type/annotation specific retrieval of bean names works.
	 *
	 * 包括bean定义名称和手动注册的单例实例名称，bean定义名称始终排在第一位，类似于对于特定类型/注解的bean名称检索如何工作。
	 *
	 * @return the composite iterator for the bean names view
	 * @since 4.1.2
	 * @see #containsBeanDefinition
	 * @see #registerSingleton
	 * @see #getBeanNamesForType
	 * @see #getBeanNamesForAnnotation
	 */
	Iterator<String> getBeanNamesIterator();

	/**
	 * Clear the merged bean definition cache, removing entries for beans
	 * which are not considered eligible for full metadata caching yet.
	 *
	 * 清除合并的bean定义缓存，移除尚不适合进行完全元数据缓存的bean实体。
	 *
	 * <p>Typically triggered after changes to the original bean definitions,
	 * e.g. after applying a {@link BeanFactoryPostProcessor}. Note that metadata
	 * for beans which have already been created at this point will be kept around.
	 *
	 * 通常在更改原始bean定义后触发，例如，应用BeanFactoryPostProcessor后。注意，此时将保留已创建的bean的元数据。
	 *
	 * @since 4.2
	 * @see #getBeanDefinition
	 * @see #getMergedBeanDefinition
	 */
	void clearMetadataCache();

	/**
	 * Freeze all bean definitions, signalling that the registered bean definitions
	 * will not be modified or post-processed any further.
	 *
	 * 冻结所有bean定义，这意味着已注册的bean定义将不会进一步进行的改变或后期处理。
	 *
	 * <p>This allows the factory to aggressively cache bean definition metadata.
	 *
	 * 此方法允许工厂积极的缓存bean定义的元数据。
	 *
	 */
	void freezeConfiguration();

	/**
	 * Return whether this factory's bean definitions are frozen,
	 * i.e. are not supposed to be modified or post-processed any further.
	 *
	 * 返回此工厂的bean定义是否已冻结，即不应该再进一步进行的修改或后期处理。
	 *
	 * @return {@code true} if the factory's configuration is considered frozen
	 */
	boolean isConfigurationFrozen();

	/**
	 * Ensure that all non-lazy-init singletons are instantiated, also considering
	 * {@link org.springframework.beans.factory.FactoryBean FactoryBeans}.
	 * Typically invoked at the end of factory setup, if desired.
	 *
	 * 确保所有非延迟加载单例都已实例化，还考虑了FactoryBeans。如有需要的话，通常在工厂设置结束时调用。
	 *
	 * @throws BeansException if one of the singleton beans could not be created.
	 *
	 * 如果其中一个单例bean未创建。
	 *
	 * Note: This may have left the factory with some beans already initialized!
	 * Call {@link #destroySingletons()} for full cleanup in this case.
	 *
	 * 注意：调用结束时可能会存在一些已初始化的bean（This may have left the factory with some beans already initialized!）。
	 * 在这种情况下，可以调用#destorySingletons进行全面清理。
	 *
	 * @see #destroySingletons()
	 */
	void preInstantiateSingletons() throws BeansException;

}

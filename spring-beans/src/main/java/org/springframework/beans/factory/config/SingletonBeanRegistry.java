/*
 * Copyright 2002-2015 the original author or authors.
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

import org.springframework.lang.Nullable;

/**
 * Interface that defines a registry for shared bean instances.
 * Can be implemented by {@link org.springframework.beans.factory.BeanFactory}
 * implementations in order to expose their singleton management facility
 * in a uniform manner.
 *
 * 为共享bean实例定义注册表的接口。可以由实现了BeanFactory接口的实现来实现此接口，以便用统一的方式来公开其单例管理设施(facility)。
 *
 * <p>The {@link ConfigurableBeanFactory} interface extends this interface.
 *
 * ConfigurableBeanFactory接口继承了此接口
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see ConfigurableBeanFactory
 * @see org.springframework.beans.factory.support.DefaultSingletonBeanRegistry
 * @see org.springframework.beans.factory.support.AbstractBeanFactory
 */
public interface SingletonBeanRegistry {

	/**
	 * Register the given existing object as singleton in the bean registry,
	 * under the given bean name.
	 *
	 * 在给定的名称下，在bean注册表中将给定的现有对象注册为单例。
	 *
	 * <p>The given instance is supposed to be fully initialized; the registry
	 * will not perform any initialization callbacks (in particular, it won't
	 * call InitializingBean's {@code afterPropertiesSet} method).
	 * The given instance will not receive any destruction callbacks
	 * (like DisposableBean's {@code destroy} method) either.
	 *
	 * 给定的实例应该是完全初始化过的；
	 * 注册表不会执行任何初始化回调（特别是，它不会调用InitializingBean的afterPropertiesSet方法）。
	 * 同时给定的实例也不会接收到任何销毁回调（像DisposableBean的destroy方法）。
	 *
	 * <p>When running within a full BeanFactory: <b>Register a bean definition
	 * instead of an existing instance if your bean is supposed to receive
	 * initialization and/or destruction callbacks.</b>
	 *
	 * 当一个完整的BeanFactory正在运行时：如果你的bean应该接收到初始化和(或)销毁回调，此时应该注册一个bean定义而不是一个已现有的实例。
	 *
	 * <p>Typically invoked during registry configuration, but can also be used
	 * for runtime registration of singletons. As a consequence, a registry
	 * implementation should synchronize singleton access; it will have to do
	 * this anyway if it supports a BeanFactory's lazy initialization of singletons.
	 *
	 * 此方法通常会在注册表配置期间调用，但在运行时(runtime)注册单例时也会被用到。因此，注册表实现应该同步访问单例；
	 * 如果支持BeanFactory单例的延迟初始化，你就必须使用上述方式（同步访问单例）。
	 *
	 * @param beanName the name of the bean bean名称
	 * @param singletonObject the existing singleton object 现有的的单例对象
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet
	 * @see org.springframework.beans.factory.DisposableBean#destroy
	 * @see org.springframework.beans.factory.support.BeanDefinitionRegistry#registerBeanDefinition
	 */
	void registerSingleton(String beanName, Object singletonObject);

	/**
	 * Return the (raw) singleton object registered under the given name.
	 *
	 * 返回注册在给定名称下的原始单例对象。
	 *
	 * <p>Only checks already instantiated singletons; does not return an Object
	 * for singleton bean definitions which have not been instantiated yet.
	 *
	 * 仅检查已实例话的单例；不会返回还未初始化的单例bean定义。
	 *
	 * <p>The main purpose of this method is to access manually registered singletons
	 * (see {@link #registerSingleton}). Can also be used to access a singleton
	 * defined by a bean definition that already been created, in a raw fashion.
	 *
	 * 此方法的主要目的是为了获取手动注册的单例（参看 #registerSingleton）。也能以原始的方式获取已创建的bean定义所定义的单例。
	 *
	 * <p><b>NOTE:</b> This lookup method is not aware of FactoryBean prefixes or aliases.
	 * You need to resolve the canonical bean name first before obtaining the singleton instance.
	 *
	 * 注意：此检索方法不知道BeanFactory的前缀或者别名。你需要在获取单例前，先把beanName转为规范bean名称。
	 *
	 * @param beanName the name of the bean to look for 所要查找的bean名称
	 * @return the registered singleton object, or {@code null} if none found 已注册的单例对象，如果不存在返回null
	 * @see ConfigurableListableBeanFactory#getBeanDefinition
	 */
	@Nullable
	Object getSingleton(String beanName);

	/**
	 * Check if this registry contains a singleton instance with the given name.
	 *
	 * 检查此注册表中是否存在给定名称的单例。
	 *
	 * <p>Only checks already instantiated singletons; does not return {@code true}
	 * for singleton bean definitions which have not been instantiated yet.
	 *
	 * 仅检查已初始化的单例；对于还未实例化的单例bean定义不会返回true。
	 *
	 * <p>The main purpose of this method is to check manually registered singletons
	 * (see {@link #registerSingleton}). Can also be used to check whether a
	 * singleton defined by a bean definition has already been created.
	 *
	 * 此方法的主要目的是检查手动注入的单例（参看 #registerSingleton）。也可以用于检查由bean定义所定义的单例是否已创建。
	 *
	 * <p>To check whether a bean factory contains a bean definition with a given name,
	 * use ListableBeanFactory's {@code containsBeanDefinition}. Calling both
	 * {@code containsBeanDefinition} and {@code containsSingleton} answers
	 * whether a specific bean factory contains a local bean instance with the given name.
	 *
	 * 要检查bean工厂（factory）中是否存在给定名称的bean定义，请使用ListableBeanFactory的containsBeanDefinition方法。
	 * 调用containsBeanDefinition和containsSingleton方法来回答指定工厂（factory）中是否存在给定名称的本地bean实例。
	 *
	 * <p>Use BeanFactory's {@code containsBean} for general checks whether the
	 * factory knows about a bean with a given name (whether manually registered singleton
	 * instance or created by bean definition), also checking ancestor factories.
	 *
	 * 使用BeanFactory的containsBean方法来对工厂(factory)是否存在给定名称的bean进行一般性检查（单例是手动注册的还是由bean定义创建），当然也会对祖先(ancestor)工厂进行此类检查。
	 *
	 * <p><b>NOTE:</b> This lookup method is not aware of FactoryBean prefixes or aliases.
	 * You need to resolve the canonical bean name first before checking the singleton status.
	 *
	 * 注意：次检索方法不知道FactoryBean的前缀或别名。在检查单例状态前你需要把beanName处理成规范bean名称。
	 *
	 * @param beanName the name of the bean to look for
	 * @return if this bean factory contains a singleton instance with the given name
	 * @see #registerSingleton
	 * @see org.springframework.beans.factory.ListableBeanFactory#containsBeanDefinition
	 * @see org.springframework.beans.factory.BeanFactory#containsBean
	 */
	boolean containsSingleton(String beanName);

	/**
	 * Return the names of singleton beans registered in this registry.
	 *
	 * 返回在注册表中注册过的单例bean的名称。
	 *
	 * <p>Only checks already instantiated singletons; does not return names
	 * for singleton bean definitions which have not been instantiated yet.
	 *
	 * 仅检查已初始化过的单例；还未实例化的单例bean定义的名称不会返回。
	 *
	 * <p>The main purpose of this method is to check manually registered singletons
	 * (see {@link #registerSingleton}). Can also be used to check which singletons
	 * defined by a bean definition have already been created.
	 *
	 * 次方法的主要目的是检查手动注册的单例（参看 #registerSingleton）。也可用于检查已创建的由bean定义所定义的bean。
	 *
	 * @return the list of names as a String array (never {@code null}) 作为字符串数组的名称列表
	 * @see #registerSingleton
	 * @see org.springframework.beans.factory.support.BeanDefinitionRegistry#getBeanDefinitionNames
	 * @see org.springframework.beans.factory.ListableBeanFactory#getBeanDefinitionNames
	 */
	String[] getSingletonNames();

	/**
	 * Return the number of singleton beans registered in this registry.
	 *
	 * 返回在注册表中注册的单例bean数量。
	 *
	 * <p>Only checks already instantiated singletons; does not count
	 * singleton bean definitions which have not been instantiated yet.
	 *
	 * 仅检查已实例化的单例；还未实例化的单例bean定义并不计算在内。
	 *
	 * <p>The main purpose of this method is to check manually registered singletons
	 * (see {@link #registerSingleton}). Can also be used to count the number of
	 * singletons defined by a bean definition that have already been created.
	 *
	 * 此方法的主要目的是检查手动注册的单例（参看 #registerSingleton）。也可以用来计算已创建的由bean定义所定义的单例数量。
	 *
	 * @return the number of singleton beans
	 * @see #registerSingleton
	 * @see org.springframework.beans.factory.support.BeanDefinitionRegistry#getBeanDefinitionCount
	 * @see org.springframework.beans.factory.ListableBeanFactory#getBeanDefinitionCount
	 */
	int getSingletonCount();

	/**
	 * Return the singleton mutex used by this registry (for external collaborators).
	 *
	 * 返回此注册表使用的单例信号量（为外部协作者）。
	 *
	 * @return the mutex object (never {@code null}) 信号量对象（不为null）
	 * @since 4.2
	 */
	Object getSingletonMutex();

}

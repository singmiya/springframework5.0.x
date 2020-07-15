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

package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

/**
 * Subinterface of {@link BeanPostProcessor} that adds a before-destruction callback.
 *
 * BeanPostProcessor子接口，用于添加销毁前（before-destruction）回调。
 *
 * <p>The typical usage will be to invoke custom destruction callbacks on
 * specific bean types, matching corresponding initialization callbacks.
 *
 * 典型的用法是，在特定类型的bean上调用自定义的销毁回调，并与相应的初始化回调匹配。
 *
 * @author Juergen Hoeller
 * @since 1.0.1
 */
public interface DestructionAwareBeanPostProcessor extends BeanPostProcessor {

	/**
	 * Apply this BeanPostProcessor to the given bean instance before its
	 * destruction, e.g. invoking custom destruction callbacks.
	 *
	 * 在BeanPostProcessor销毁前，将其应用于给定的bean实例，例如，调用自定义销毁回调。
	 *
	 * <p>Like DisposableBean's {@code destroy} and a custom destroy method, this
	 * callback will only apply to beans which the container fully manages the
	 * lifecycle for. This is usually the case for singletons and scoped beans.
	 *
	 * 像DisposableBean的销毁和自定义销毁方法一样，此回调仅适用于容器管理其生命周期的bean。单例和作用域bean通常是这种情况。
	 *
	 * @param bean the bean instance to be destroyed
	 * @param beanName the name of the bean
	 * @throws org.springframework.beans.BeansException in case of errors
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#setDestroyMethodName(String)
	 */
	void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException;

	/**
	 * Determine whether the given bean instance requires destruction by this
	 * post-processor.
	 *
	 * 通过此后置处理器（post-processor）确定给定bean实例是否需要销毁。
	 *
	 * <p>The default implementation returns {@code true}. If a pre-5 implementation
	 * of {@code DestructionAwareBeanPostProcessor} does not provide a concrete
	 * implementation of this method, Spring silently assumes {@code true} as well.
	 *
	 * 默认实现返回true。如果 DestructionAwareBeanPostProcessor 版本5之前的实现不提供此方法的具体实现，则Spring也会默默的假定为true。
	 *
	 * @param bean the bean instance to check
	 * @return {@code true} if {@link #postProcessBeforeDestruction} is supposed to
	 * be called for this bean instance eventually, or {@code false} if not needed
	 * @since 4.3
	 */
	default boolean requiresDestruction(Object bean) {
		return true;
	}

}

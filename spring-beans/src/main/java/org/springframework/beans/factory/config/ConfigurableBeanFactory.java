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

import java.beans.PropertyEditor;
import java.security.AccessControlContext;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.util.StringValueResolver;

/**
 * Configuration interface to be implemented by most bean factories. Provides
 * facilities to configure a bean factory, in addition to the bean factory
 * client methods in the {@link org.springframework.beans.factory.BeanFactory}
 * interface.
 *
 * 配置（Configuration）接口将被大部分bean工厂（factories）实现。除了BeanFactory接口中的bean工厂（factory）客户端方法之外，
 * 还提供了配置bean工厂的工具。
 *
 * <p>This bean factory interface is not meant to be used in normal application
 * code: Stick to {@link org.springframework.beans.factory.BeanFactory} or
 * {@link org.springframework.beans.factory.ListableBeanFactory} for typical
 * needs. This extended interface is just meant to allow for framework-internal
 * plug'n'play and for special access to bean factory configuration methods.
 *
 * 此bean工厂接口不适合用于常规应用代码中：坚持使用BeanFactory和ListableBeanFactory来满足常规需求。
 * 此扩展接口仅用于允许框架内部即插即用（plug'n'play）操作及对bean工厂配置方法的特殊访问。
 *
 * @author Juergen Hoeller
 * @since 03.11.2003
 * @see org.springframework.beans.factory.BeanFactory
 * @see org.springframework.beans.factory.ListableBeanFactory
 * @see ConfigurableListableBeanFactory
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

	/**
	 * Scope identifier for the standard singleton scope: "singleton".
	 * Custom scopes can be added via {@code registerScope}.
	 *
	 * 标准单例作用域的作用域标识符："singleton"。
	 * 可以通过 registerScope 添加自定义作用域。
	 *
	 * @see #registerScope
	 */
	String SCOPE_SINGLETON = "singleton";

	/**
	 * Scope identifier for the standard prototype scope: "prototype".
	 * Custom scopes can be added via {@code registerScope}.
	 *
	 * 标准原型作用域的作用域标识符："prototype"。
	 * 可以通过 registerScope 添加自定义作用域。
	 *
	 * @see #registerScope
	 */
	String SCOPE_PROTOTYPE = "prototype";


	/**
	 * Set the parent of this bean factory.
	 * <p>Note that the parent cannot be changed: It should only be set outside
	 * a constructor if it isn't available at the time of factory instantiation.
	 *
	 * 为此bean工厂设置父工厂。
	 * 注意，父工厂无法改变：仅当在工厂实例化时不可用的时候，才应在构造方法外部设置它。
	 *
	 * @param parentBeanFactory the parent BeanFactory
	 * @throws IllegalStateException if this factory is already associated with
	 * a parent BeanFactory 如果此工厂已与父BeanFactory关联
	 * @see #getParentBeanFactory()
	 */
	void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;

	/**
	 * Set the class loader to use for loading bean classes.
	 * Default is the thread context class loader.
	 *
	 * 设置用于加载bean类的类加载器。默认是线程上下文类加载器（thread context class loader）。
	 *
	 * <p>Note that this class loader will only apply to bean definitions
	 * that do not carry a resolved bean class yet. This is the case as of
	 * Spring 2.0 by default: Bean definitions only carry bean class names,
	 * to be resolved once the factory processes the bean definition.
	 *
	 * 注意，此类加载器仅适用于未包含已解析的bean类的bean定义。
	 * Spring2.0默认情况下就是这样：Bean定义仅包含bean类名，一旦工厂处理完bean定义就可就行解析。
	 *
	 * @param beanClassLoader the class loader to use,
	 * or {@code null} to suggest the default class loader
	 *
	 *  要使用的类加载器，如果为null则建议使用默认类加载器
	 */
	void setBeanClassLoader(@Nullable ClassLoader beanClassLoader);

	/**
	 * Return this factory's class loader for loading bean classes
	 * (only {@code null} if even the system ClassLoader isn't accessible).
	 *
	 * 返回此工厂用于加载bean类的类加载器（仅在连系统类加载器（ClassLoader）也无法获取时返回null）。
	 *
	 * @see org.springframework.util.ClassUtils#forName(String, ClassLoader)
	 */
	@Nullable
	ClassLoader getBeanClassLoader();

	/**
	 * Specify a temporary ClassLoader to use for type matching purposes.
	 * Default is none, simply using the standard bean ClassLoader.
	 *
	 * 指定一个用于匹配类型的临时类加载器（ClassLoader）。默认是不存在，只需使用标准的bean加载器（ClassLoader）。
	 *
	 * <p>A temporary ClassLoader is usually just specified if
	 * <i>load-time weaving</i> is involved, to make sure that actual bean
	 * classes are loaded as lazily as possible. The temporary loader is
	 * then removed once the BeanFactory completes its bootstrap phase.
	 *
	 * 仅在加载时间交织时（load-time weaving），才指定临时类加载器（ClassLoader），确保尽可能延迟加载真正的bean类。
	 * 一旦BeanFactory的引导阶段完成，就会把临时加载器移除。
	 *
	 * @since 2.5
	 */
	void setTempClassLoader(@Nullable ClassLoader tempClassLoader);

	/**
	 * Return the temporary ClassLoader to use for type matching purposes,
	 * if any.
	 *
	 * 如果存在的话，就返回用于匹配类型的临时类加载器（ClassLoader）。
	 *
	 * @since 2.5
	 */
	@Nullable
	ClassLoader getTempClassLoader();

	/**
	 * Set whether to cache bean metadata such as given bean definitions
	 * (in merged fashion) and resolved bean classes. Default is on.
	 *
	 * 设置是否缓存bean元数据，例如给定的bean定义（in merged fashion）和解析过的bean类。默认开启
	 *
	 * <p>Turn this flag off to enable hot-refreshing of bean definition objects
	 * and in particular bean classes. If this flag is off, any creation of a bean
	 * instance will re-query the bean class loader for newly resolved classes.
	 *
	 * 关闭此标识可以启用bean定义对象，特别是bean类的热刷新功能。
	 * 如果关闭此标识，则任何bean实例的创建都会重新查询bean类加载器以获取全新解析的类。
	 *
	 */
	void setCacheBeanMetadata(boolean cacheBeanMetadata);

	/**
	 * Return whether to cache bean metadata such as given bean definitions
	 * (in merged fashion) and resolved bean classes.
	 *
	 * 返回是否缓存bean元数据，例如给定的bean定义（in merged fashion）和解析过的bean类。
	 *
	 */
	boolean isCacheBeanMetadata();

	/**
	 * Specify the resolution strategy for expressions in bean definition values.
	 *
	 * 为bean定义之中的表达式指定解决策略。
	 *
	 * <p>There is no expression support active in a BeanFactory by default.
	 * An ApplicationContext will typically set a standard expression strategy
	 * here, supporting "#{...}" expressions in a Unified EL compatible style.
	 *
	 * 默认情况下，BeanFactory中没有激活对表达式的支持。
	 * ApplicationContext通常会在此设置标准的表达式策略，以统一的EL兼容方式来支持"#{...}"表达式。
	 *
	 * @since 3.0
	 */
	void setBeanExpressionResolver(@Nullable BeanExpressionResolver resolver);

	/**
	 * Return the resolution strategy for expressions in bean definition values.
	 *
	 * 返回在bean定义值中表达式的解决策略。
	 *
	 * @since 3.0
	 */
	@Nullable
	BeanExpressionResolver getBeanExpressionResolver();

	/**
	 * Specify a Spring 3.0 ConversionService to use for converting
	 * property values, as an alternative to JavaBeans PropertyEditors.
	 *
	 * 指定一个Spring 3.0 ConversionService来转换属性值，代替JavaBeans PropertyEditors。
	 *
	 * @since 3.0
	 */
	void setConversionService(@Nullable ConversionService conversionService);

	/**
	 * Return the associated ConversionService, if any.
	 *
	 * 如果存在，则返回与之关联的ConversionService。
	 *
	 * @since 3.0
	 */
	@Nullable
	ConversionService getConversionService();

	/**
	 * Add a PropertyEditorRegistrar to be applied to all bean creation processes.
	 *
	 * 添加一个PropertyEditorRegistrar应用于所有的bean创建过程。
	 *
	 * <p>Such a registrar creates new PropertyEditor instances and registers them
	 * on the given registry, fresh for each bean creation attempt. This avoids
	 * the need for synchronization on custom editors; hence, it is generally
	 * preferable to use this method instead of {@link #registerCustomEditor}.
	 *
	 * 这样的registrar会创建新的PropertyEditor实例，并将它们注册到给定的注册表中，每一次bean创建都是新的。
	 * 这样避免了在自定义editors上进行同步操作的需要；因此，通常情况下最好使用此方法代替#registerCustomEditor.
	 *
	 * @param registrar the PropertyEditorRegistrar to register
	 */
	void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);

	/**
	 * Register the given custom property editor for all properties of the
	 * given type. To be invoked during factory configuration.
	 *
	 * 为给定类型的所有属性注册给定的自定义属性编辑器。在工厂配置期间被调用。
	 *
	 * <p>Note that this method will register a shared custom editor instance;
	 * access to that instance will be synchronized for thread-safety. It is
	 * generally preferable to use {@link #addPropertyEditorRegistrar} instead
	 * of this method, to avoid for the need for synchronization on custom editors.
	 *
	 * 注意，此方法会注册一个共享的自定义编辑器实例；为确保线程安全，访问此实例将会进行同步操作。
	 * 为避免在自定义编辑器上进行同步操作的需要，通常情况下最好使用#addPropertyEditorRegistrar代替此方法。
	 *
	 * @param requiredType type of the property
	 * @param propertyEditorClass the {@link PropertyEditor} class to register
	 */
	void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass);

	/**
	 * Initialize the given PropertyEditorRegistry with the custom editors
	 * that have been registered with this BeanFactory.
	 *
	 * 使用已注册在BeanFactory中的自定义编辑器初始化给定的PropertyEditorRegistry。
	 *
	 * @param registry the PropertyEditorRegistry to initialize
	 */
	void copyRegisteredEditorsTo(PropertyEditorRegistry registry);

	/**
	 * Set a custom type converter that this BeanFactory should use for converting
	 * bean property values, constructor argument values, etc.
	 *
	 * 设置此BeanFactory用于转换bean属性值、构造参数值等的自已定义类型转换器。
	 *
	 * <p>This will override the default PropertyEditor mechanism and hence make
	 * any custom editors or custom editor registrars irrelevant.
	 *
	 * 这会覆盖默认的PropertyEditor机制，因此，任何自定义编辑器或自定义编辑器注册器（registrars）都不相关。
	 *
	 * @see #addPropertyEditorRegistrar
	 * @see #registerCustomEditor
	 * @since 2.5
	 */
	void setTypeConverter(TypeConverter typeConverter);

	/**
	 * Obtain a type converter as used by this BeanFactory. This may be a fresh
	 * instance for each call, since TypeConverters are usually <i>not</i> thread-safe.
	 *
	 * 获取此BeanFactory使用的类型转换器。因为TypeConverters通常是非线程安全的，所以每次调用都会返回一个全新的实例。
	 *
	 * <p>If the default PropertyEditor mechanism is active, the returned
	 * TypeConverter will be aware of all custom editors that have been registered.
	 *
	 * 如果的默认的PropertyEditor机制处于活动状态，返回的TypeConverter将会知道所有已注册的自定义编辑器。
	 *
	 * @since 2.5
	 */
	TypeConverter getTypeConverter();

	/**
	 * Add a String resolver for embedded values such as annotation attributes.
	 *
	 * 为嵌入值添加String解析器，例如注解属性。
	 *
	 * @param valueResolver the String resolver to apply to embedded values 用于嵌入值的String解析器
	 * @since 3.0
	 */
	void addEmbeddedValueResolver(StringValueResolver valueResolver);

	/**
	 * Determine whether an embedded value resolver has been registered with this
	 * bean factory, to be applied through {@link #resolveEmbeddedValue(String)}.
	 *
	 * 确定此bean工厂是否已通过#resolveEmbeddedValue(String)注册过嵌入值解析器。
	 *
	 * @since 4.3
	 */
	boolean hasEmbeddedValueResolver();

	/**
	 * Resolve the given embedded value, e.g. an annotation attribute.
	 *
	 * 解析给定嵌入值，例如，注解属性。
	 *
	 * @param value the value to resolve
	 * @return the resolved value (may be the original value as-is)
	 * @since 3.0
	 */
	@Nullable
	String resolveEmbeddedValue(String value);

	/**
	 * Add a new BeanPostProcessor that will get applied to beans created
	 * by this factory. To be invoked during factory configuration.
	 *
	 * 添加一个新的的BeanPostProcessor，它将用于此bean工厂创建的bean。在工厂配置期间被调用。
	 *
	 * <p>Note: Post-processors submitted here will be applied in the order of
	 * registration; any ordering semantics expressed through implementing the
	 * {@link org.springframework.core.Ordered} interface will be ignored. Note
	 * that autodetected post-processors (e.g. as beans in an ApplicationContext)
	 * will always be applied after programmatically registered ones.
	 *
	 * 注意：此处提交的后置处理器（Post-processor）将按照注册的顺序应用；通过Ordered接口实现来表达的任何排序语义都将被忽略。
	 * 注意，自动检测到的后置处理器（post-processor）（例如，ApplicationContext中的bean），始终都会在用代码注册的处理器之后应用。
	 *
	 * @param beanPostProcessor the post-processor to register
	 */
	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

	/**
	 * Return the current number of registered BeanPostProcessors, if any.
	 *
	 * 如果存在，则返回已注册的BeanPostProcessors数量。
	 *
	 */
	int getBeanPostProcessorCount();

	/**
	 * Register the given scope, backed by the given Scope implementation.
	 *
	 * 注册给定的作用域，次作用域由给定的作用域实现支持。
	 *
	 * @param scopeName the scope identifier
	 * @param scope the backing Scope implementation
	 */
	void registerScope(String scopeName, Scope scope);

	/**
	 * Return the names of all currently registered scopes.
	 *
	 * 返回当前已注册过的作用域名称。
	 *
	 * <p>This will only return the names of explicitly registered scopes.
	 * Built-in scopes such as "singleton" and "prototype" won't be exposed.
	 *
	 * 此方法只返回显示注册的作用域名称。像"singleton"和"prototype"这样内置的作用域则不会公开。
	 *
	 * @return the array of scope names, or an empty array if none
	 * @see #registerScope
	 */
	String[] getRegisteredScopeNames();

	/**
	 * Return the Scope implementation for the given scope name, if any.
	 *
	 * 如果存在，则返回给定名称作用域的Scope实现。
	 *
	 * <p>This will only return explicitly registered scopes.
	 * Built-in scopes such as "singleton" and "prototype" won't be exposed.
	 *
	 * 只返回显示注册过的作用域，像"singleton"和"prototype"这样内置的作用域则不会公开。
	 *
	 * @param scopeName the name of the scope
	 * @return the registered Scope implementation, or {@code null} if none
	 * @see #registerScope
	 */
	@Nullable
	Scope getRegisteredScope(String scopeName);

	/**
	 * Provides a security access control context relevant to this factory.
	 *
	 * 提供与此工厂相关的安全访问控制上下文。
	 *
	 * @return the applicable AccessControlContext (never {@code null})
	 * @since 3.0
	 */
	AccessControlContext getAccessControlContext();

	/**
	 * Copy all relevant configuration from the given other factory.
	 *
	 * 从给定的其他工厂中复制所有的相关配置。
	 *
	 * <p>Should include all standard configuration settings as well as
	 * BeanPostProcessors, Scopes, and factory-specific internal settings.
	 * Should not include any metadata of actual bean definitions,
	 * such as BeanDefinition objects and bean name aliases.
	 *
	 * 应该包含所有的标准配置设置，同时还有BeanPostProcessor，Scopes和工厂特定的内置设置。
	 *
	 * @param otherFactory the other BeanFactory to copy from
	 */
	void copyConfigurationFrom(ConfigurableBeanFactory otherFactory);

	/**
	 * Given a bean name, create an alias. We typically use this method to
	 * support names that are illegal within XML ids (used for bean names).
	 *
	 * 为给定bean名称创建一个别名。通常的，我们使用此方法来支持XML id（用于bean名称）中不合法的名称。
	 *
	 * <p>Typically invoked during factory configuration, but can also be
	 * used for runtime registration of aliases. Therefore, a factory
	 * implementation should synchronize alias access.
	 *
	 * 通常会在工厂配置期间调用，但也可以用在别名的运行时注册。因此，工厂应实现同步别名访问。
	 *
	 * @param beanName the canonical name of the target bean
	 * @param alias the alias to be registered for the bean
	 * @throws BeanDefinitionStoreException if the alias is already in use
	 */
	void registerAlias(String beanName, String alias) throws BeanDefinitionStoreException;

	/**
	 * Resolve all alias target names and aliases registered in this
	 * factory, applying the given StringValueResolver to them.
	 *
	 * 使用给定的StringValueResolver来解析目标名册的所有别名和已在此工厂中注册的别名。
	 *
	 * <p>The value resolver may for example resolve placeholders
	 * in target bean names and even in alias names.
	 *
	 * 例如，值解析器可能会解析目标bean名称甚至是别名中的占位符。
	 *
	 * @param valueResolver the StringValueResolver to apply
	 * @since 2.5
	 */
	void resolveAliases(StringValueResolver valueResolver);

	/**
	 * Return a merged BeanDefinition for the given bean name,
	 * merging a child bean definition with its parent if necessary.
	 * Considers bean definitions in ancestor factories as well.
	 *
	 * 根据给定名称返回一个合并的BeanDefinition，如有必要将子bean定义与其父bean合并。
	 *
	 * @param beanName the name of the bean to retrieve the merged definition for
	 *                 用于检索要合并定义的bean名称
	 * @return a (potentially merged) BeanDefinition for the given bean
	 * @throws NoSuchBeanDefinitionException if there is no bean definition with the given name
	 * @since 2.5
	 */
	BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;

	/**
	 * Determine whether the bean with the given name is a FactoryBean.
	 *
	 * 确定给定名称的bean是否是一个FactoryBean。
	 *
	 * @param name the name of the bean to check
	 * @return whether the bean is a FactoryBean
	 * ({@code false} means the bean exists but is not a FactoryBean)
	 * @throws NoSuchBeanDefinitionException if there is no bean with the given name
	 * @since 2.5
	 */
	boolean isFactoryBean(String name) throws NoSuchBeanDefinitionException;

	/**
	 * Explicitly control the current in-creation status of the specified bean.
	 * For container-internal use only.
	 *
	 * 显示的控制指定bean当前的 in-creation 状态。
	 * 仅用于容器内部。
	 *
	 * @param beanName the name of the bean
	 * @param inCreation whether the bean is currently in creation
	 * @since 3.1
	 */
	void setCurrentlyInCreation(String beanName, boolean inCreation);

	/**
	 * Determine whether the specified bean is currently in creation.
	 *
	 * 确定指定bean当前是否处于创建状态。
	 *
	 * @param beanName the name of the bean
	 * @return whether the bean is currently in creation
	 * @since 2.5
	 */
	boolean isCurrentlyInCreation(String beanName);

	/**
	 * Register a dependent bean for the given bean,
	 * to be destroyed before the given bean is destroyed.
	 *
	 * 为给定bean创建依赖bean，并在给定bean销毁之前将其销毁。
	 *
	 * @param beanName the name of the bean
	 * @param dependentBeanName the name of the dependent bean
	 * @since 2.5
	 */
	void registerDependentBean(String beanName, String dependentBeanName);

	/**
	 * Return the names of all beans which depend on the specified bean, if any.
	 *
	 * 如果存在，返回依赖指定bean的所有bean名称。
	 *
	 * @param beanName the name of the bean
	 * @return the array of dependent bean names, or an empty array if none
	 * @since 2.5
	 */
	String[] getDependentBeans(String beanName);

	/**
	 * Return the names of all beans that the specified bean depends on, if any.
	 *
	 * 如果存在，返回指定bean依赖的所有bean名称。
	 *
	 * @param beanName the name of the bean
	 * @return the array of names of beans which the bean depends on,
	 * or an empty array if none
	 * @since 2.5
	 */
	String[] getDependenciesForBean(String beanName);

	/**
	 * Destroy the given bean instance (usually a prototype instance
	 * obtained from this factory) according to its bean definition.
	 *
	 * 根据它的bean定义来销毁指定的bean实例（通常是此工厂获取的原型实例）。
	 *
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 *
	 * 在销毁过程中出现的的任何异常都应被捕获并记录下来，而不是传递给此方法的调用者。
	 *
	 * @param beanName the name of the bean definition
	 * @param beanInstance the bean instance to destroy
	 */
	void destroyBean(String beanName, Object beanInstance);

	/**
	 * Destroy the specified scoped bean in the current target scope, if any.
	 *
	 * 如果存在，销毁当前目标作用域中的特定作用域的bean。
	 *
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 *
	 * 销毁过程中出现的任何异常都应被捕获并记录下来，而不是传递给此方法的调用者。
	 *
	 * @param beanName the name of the scoped bean
	 */
	void destroyScopedBean(String beanName);

	/**
	 * Destroy all singleton beans in this factory, including inner beans that have
	 * been registered as disposable. To be called on shutdown of a factory.
	 *
	 * 销毁此工厂中的所有单例bean，包含已注册的一次性内部bean。将会在关闭工厂（factory）时调用。
	 *
	 * <p>Any exception that arises during destruction should be caught
	 * and logged instead of propagated to the caller of this method.
	 *
	 * 销毁过程中出现的任何异常都应被捕获并记录下来，而不是传递给此方法的调用者。
	 *
	 */
	void destroySingletons();

}

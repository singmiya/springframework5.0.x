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

package org.springframework.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

/**
 * This class can be used to parse other classes containing constant definitions
 * in public static final members. The {@code asXXXX} methods of this class
 * allow these constant values to be accessed via their string names.
 *
 * 这个类用来解析其他类中定义的 public static final 的常量。类中的asXXXX方法可以通过其他类中的常量名称获取其常量值。
 *
 * <p>Consider class Foo containing {@code public final static int CONSTANT1 = 66;}
 * An instance of this class wrapping {@code Foo.class} will return the constant value
 * of 66 from its {@code asNumber} method given the argument {@code "CONSTANT1"}.
 *
 * 例如：Foo类包含常量定义：public static final int CONSTANT1 = 66;
 *
 * 包装了Foo.class本类实例，其asNumber方法将通过给定的参数"CONSTANT1"返回对应的常量值66。
 *
 * <p>This class is ideal for use in PropertyEditors, enabling them to
 * recognize the same names as the constants themselves, and freeing them
 * from maintaining their own mapping.
 *
 * 这个类很适合用在PropertyEditors中，使他们能够识别与常量相同的名称，且不用维护他们自己的映射。
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 16.03.2003
 */
public class Constants {

	/** The name of the introspected class */
	// 反射类名称
	private final String className;

	/** Map from String field name to object value */
	// 字符串字段名称映射到对象值
	private final Map<String, Object> fieldCache = new HashMap<>();


	/**
	 * Create a new Constants converter class wrapping the given class.
	 * <p>All <b>public</b> static final variables will be exposed, whatever their type.
	 *
	 * 创建一个包装给定类的Constants转换器类。
	 *
	 * @param clazz the class to analyze 要分析的类
	 * @throws IllegalArgumentException if the supplied {@code clazz} is {@code null}
	 */
	public Constants(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		this.className = clazz.getName();
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			// 判断字段是否是 public static final类型
			if (ReflectionUtils.isPublicStaticFinal(field)) {
				String name = field.getName();
				try {
					Object value = field.get(null);
					this.fieldCache.put(name, value);
				}
				catch (IllegalAccessException ex) {
					// just leave this field and continue
				}
			}
		}
	}


	/**
	 * Return the name of the analyzed class.
	 */
	// 返回分析类名称
	public final String getClassName() {
		return this.className;
	}

	/**
	 * Return the number of constants exposed.
	 */
	// 返回向外公开的常量数量
	public final int getSize() {
		return this.fieldCache.size();
	}

	/**
	 * Exposes the field cache to subclasses:
	 * a Map from String field name to object value.
	 */
	// 向子类公开字段缓存Map
	protected final Map<String, Object> getFieldCache() {
		return this.fieldCache;
	}


	/**
	 * Return a constant value cast to a Number.
	 *
	 * 返回转为Number类型的常量值
	 *
	 * @param code the name of the field (never {@code null}) 字段名称（不为null）
	 * @return the Number value
	 * @see #asObject
	 * @throws ConstantException if the field name wasn't found
	 * or if the type wasn't compatible with Number 如果字段不存在或者与Number类型不匹配
	 */
	public Number asNumber(String code) throws ConstantException {
		Object obj = asObject(code);
		if (!(obj instanceof Number)) {
			throw new ConstantException(this.className, code, "not a Number");
		}
		return (Number) obj;
	}

	/**
	 * Return a constant value as a String.
	 *
	 * 返回转为String类型的常量值
	 *
	 * @param code the name of the field (never {@code null}) 字段名称（不为null）
	 * @return the String value
	 * Works even if it's not a string (invokes {@code toString()}). 即使不是String类型也正常运行（会调用toString()方法）
	 * @see #asObject
	 * @throws ConstantException if the field name wasn't found 未找到字段名称
	 */
	public String asString(String code) throws ConstantException {
		return asObject(code).toString();
	}

	/**
	 * Parse the given String (upper or lower case accepted) and return
	 * the appropriate value if it's the name of a constant field in the
	 * class that we're analysing.
	 *
	 * 如果给定的字符串（接受大写或者小写）与我们正在分析的类中常量名称相同，则返回相应的常量值。
	 *
	 * @param code the name of the field (never {@code null}) 字段名称（不为null）
	 * @return the Object value
	 * @throws ConstantException if there's no such field 如果字段不存在
	 */
	public Object asObject(String code) throws ConstantException {
		Assert.notNull(code, "Code must not be null");
		String codeToUse = code.toUpperCase(Locale.ENGLISH);
		Object val = this.fieldCache.get(codeToUse);
		if (val == null) {
			throw new ConstantException(this.className, codeToUse, "not found");
		}
		return val;
	}


	/**
	 * Return all names of the given group of constants.
	 * <p>Note that this method assumes that constants are named
	 * in accordance with the standard Java convention for constant
	 * values (i.e. all uppercase). The supplied {@code namePrefix}
	 * will be uppercased (in a locale-insensitive fashion) prior to
	 * the main logic of this method kicking in.
	 *
	 * 返回给定组的所有常量名称（拥有相同前缀）。
	 * 注意：该方法假定常量命名都是符合标准Java约定的常量值（如：全大写）。
	 *
	 * 方法参数 namePrefix 会在进入主逻辑之前对其进行大写转换。
	 *
	 * @param namePrefix prefix of the constant names to search (may be {@code null}) 要查找的常量名称前缀（可能为null）
	 * @return the set of constant names
	 */
	public Set<String> getNames(@Nullable String namePrefix) {
		String prefixToUse = (namePrefix != null ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : "");
		Set<String> names = new HashSet<>();
		for (String code : this.fieldCache.keySet()) {
			if (code.startsWith(prefixToUse)) {
				names.add(code);
			}
		}
		return names;
	}

	/**
	 * Return all names of the group of constants for the
	 * given bean property name.
	 *
	 * 返回以bean属性名称（propertyName）为给定组的所有常量名称。
	 *
	 * @param propertyName the name of the bean property bean属性名称
	 * @return the set of values
	 * @see #propertyToConstantNamePrefix
	 */
	public Set<String> getNamesForProperty(String propertyName) {
		return getNames(propertyToConstantNamePrefix(propertyName));
	}

	/**
	 * Return all names of the given group of constants.
	 * <p>Note that this method assumes that constants are named
	 * in accordance with the standard Java convention for constant
	 * values (i.e. all uppercase). The supplied {@code nameSuffix}
	 * will be uppercased (in a locale-insensitive fashion) prior to
	 * the main logic of this method kicking in.
	 *
	 * 返回给定组的所有常量名。
	 * 该方法假定常量命名都是符合标准Java约定的常量值（例如：全大写）。
	 *
	 * 给定的参数 nameSuffix 在进入主逻辑前会对其进行大写转换。
	 *
	 * @param nameSuffix suffix of the constant names to search (may be {@code null}) 要查找的常量名称后缀（可能为null）
	 * @return the set of constant names
	 */
	public Set<String> getNamesForSuffix(@Nullable String nameSuffix) {
		String suffixToUse = (nameSuffix != null ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : "");
		Set<String> names = new HashSet<>();
		for (String code : this.fieldCache.keySet()) {
			if (code.endsWith(suffixToUse)) {
				names.add(code);
			}
		}
		return names;
	}


	/**
	 * Return all values of the given group of constants.
	 * <p>Note that this method assumes that constants are named
	 * in accordance with the standard Java convention for constant
	 * values (i.e. all uppercase). The supplied {@code namePrefix}
	 * will be uppercased (in a locale-insensitive fashion) prior to
	 * the main logic of this method kicking in.
	 *
	 * 返回给定组的所有常量值。
	 * 该方法假定常量命名都是符合标准Java约定的常量值（例如：全大写）。
	 *
	 * 给定参数 namePrefix 在进入主逻辑前会对其进行大写转换
	 *
	 * @param namePrefix prefix of the constant names to search (may be {@code null}) 要查找的常量名称前缀（可能为null）
	 * @return the set of values
	 */
	public Set<Object> getValues(@Nullable String namePrefix) {
		String prefixToUse = (namePrefix != null ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : "");
		Set<Object> values = new HashSet<>();
		this.fieldCache.forEach((code, value) -> {
			if (code.startsWith(prefixToUse)) {
				values.add(value);
			}
		});
		return values;
	}

	/**
	 * Return all values of the group of constants for the
	 * given bean property name.
	 *
	 * 返回以给定bean属性名称（propertyName）为组的所有常量值。
	 *
	 * @param propertyName the name of the bean property bean属性名称
	 * @return the set of values
	 * @see #propertyToConstantNamePrefix
	 */
	public Set<Object> getValuesForProperty(String propertyName) {
		return getValues(propertyToConstantNamePrefix(propertyName));
	}

	/**
	 * Return all values of the given group of constants.
	 * <p>Note that this method assumes that constants are named
	 * in accordance with the standard Java convention for constant
	 * values (i.e. all uppercase). The supplied {@code nameSuffix}
	 * will be uppercased (in a locale-insensitive fashion) prior to
	 * the main logic of this method kicking in.
	 *
	 * 返回给定组的所有常量值。
	 * 该方法假定常量命名都是符合标准Java约定的常量值（例如：全大写）。
	 * 在进入主逻辑前方法会对其给定的参数 nameSuffix 进行大写转换。
	 *
	 * @param nameSuffix suffix of the constant names to search (may be {@code null}) 要查找的常量名后缀（可能为null）
	 * @return the set of values
	 */
	public Set<Object> getValuesForSuffix(@Nullable String nameSuffix) {
		String suffixToUse = (nameSuffix != null ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : "");
		Set<Object> values = new HashSet<>();
		this.fieldCache.forEach((code, value) -> {
			if (code.endsWith(suffixToUse)) {
				values.add(value);
			}
		});
		return values;
	}


	/**
	 * Look up the given value within the given group of constants.
	 * <p>Will return the first match.
	 *
	 * 在给定的常量组中查找给定常量值对应的常量名。
	 * 将会返回第一个匹配到的常量名。
	 *
	 * @param value constant value to look up 要查找的常量值
	 * @param namePrefix prefix of the constant names to search (may be {@code null}) 要查找的常量名前缀
	 * @return the name of the constant field
	 * @throws ConstantException if the value wasn't found
	 */
	public String toCode(Object value, @Nullable String namePrefix) throws ConstantException {
		String prefixToUse = (namePrefix != null ? namePrefix.trim().toUpperCase(Locale.ENGLISH) : "");
		for (Map.Entry<String, Object> entry : this.fieldCache.entrySet()) {
			if (entry.getKey().startsWith(prefixToUse) && entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		throw new ConstantException(this.className, prefixToUse, value);
	}

	/**
	 * Look up the given value within the group of constants for
	 * the given bean property name. Will return the first match.
	 *
	 * 在以给定的bean属性名称（propertyName）为组的常量组中查找给定值对应的常量名称。返回第一个匹配的常量名称。
	 *
	 * @param value constant value to look up 要查找的常量值
	 * @param propertyName the name of the bean property bean属性名称
	 * @return the name of the constant field
	 * @throws ConstantException if the value wasn't found
	 * @see #propertyToConstantNamePrefix
	 */
	public String toCodeForProperty(Object value, String propertyName) throws ConstantException {
		return toCode(value, propertyToConstantNamePrefix(propertyName));
	}

	/**
	 * Look up the given value within the given group of constants.
	 * <p>Will return the first match.
	 *
	 * 在给定的常量组中查找给定值对应的常量名。
	 * 返回第一个匹配的常量名。
	 *
	 * @param value constant value to look up 要查找的常量值
	 * @param nameSuffix suffix of the constant names to search (may be {@code null}) 要查找的常量名称后缀（可能为null）
	 * @return the name of the constant field
	 * @throws ConstantException if the value wasn't found
	 */
	public String toCodeForSuffix(Object value, @Nullable String nameSuffix) throws ConstantException {
		String suffixToUse = (nameSuffix != null ? nameSuffix.trim().toUpperCase(Locale.ENGLISH) : "");
		for (Map.Entry<String, Object> entry : this.fieldCache.entrySet()) {
			if (entry.getKey().endsWith(suffixToUse) && entry.getValue().equals(value)) {
				return entry.getKey();
			}
		}
		throw new ConstantException(this.className, suffixToUse, value);
	}


	/**
	 * Convert the given bean property name to a constant name prefix.
	 * <p>Uses a common naming idiom: turning all lower case characters to
	 * upper case, and prepending upper case characters with an underscore.
	 *
	 * 把给定的 bean 属性名称（propertyName）转为常量名称前缀。
	 *
	 * 使用通用命名习惯：在大写字母前加下划线，并把所有小写字符转为大写。
	 *
	 * <p>Example: "imageSize" -> "IMAGE_SIZE"<br>
	 * Example: "imagesize" -> "IMAGESIZE".<br>
	 * Example: "ImageSize" -> "_IMAGE_SIZE".<br>
	 * Example: "IMAGESIZE" -> "_I_M_A_G_E_S_I_Z_E"
	 * @param propertyName the name of the bean property bean属性名称
	 * @return the corresponding constant name prefix 返回相应的常量名前缀
	 * @see #getValuesForProperty
	 * @see #toCodeForProperty
	 */
	public String propertyToConstantNamePrefix(String propertyName) {
		StringBuilder parsedPrefix = new StringBuilder();
		for (int i = 0; i < propertyName.length(); i++) {
			char c = propertyName.charAt(i);
			if (Character.isUpperCase(c)) {
				parsedPrefix.append("_");
				parsedPrefix.append(c);
			}
			else {
				parsedPrefix.append(Character.toUpperCase(c));
			}
		}
		return parsedPrefix.toString();
	}


	/**
	 * Exception thrown when the {@link Constants} class is asked for
	 * an invalid constant name.
	 *
	 * 当 Constants 类中被请求的常量名无效时抛出异常
	 *
	 */
	@SuppressWarnings("serial")
	public static class ConstantException extends IllegalArgumentException {

		/**
		 * Thrown when an invalid constant name is requested.
		 *
		 * 请求的常量名无效时抛出
		 *
		 * @param className name of the class containing the constant definitions
		 * @param field invalid constant name
		 * @param message description of the problem
		 */
		public ConstantException(String className, String field, String message) {
			super("Field '" + field + "' " + message + " in class [" + className + "]");
		}

		/**
		 * Thrown when an invalid constant value is looked up.
		 *
		 * 查找的常量值无效时
		 *
		 * @param className name of the class containing the constant definitions
		 * @param namePrefix prefix of the searched constant names
		 * @param value the looked up constant value
		 */
		public ConstantException(String className, String namePrefix, Object value) {
			super("No '" + namePrefix + "' field with value '" + value + "' found in class [" + className + "]");
		}
	}

}

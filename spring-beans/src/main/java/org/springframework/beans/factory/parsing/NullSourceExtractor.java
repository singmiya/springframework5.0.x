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

package org.springframework.beans.factory.parsing;

import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

/**
 * Simple implementation of {@link SourceExtractor} that returns {@code null}
 * as the source metadata.
 *
 * 返回null作为源元数据的SourceExtractor接口的简单实现。
 *
 * <p>This is the default implementation and prevents too much metadata from being
 * held in memory during normal (non-tooled) runtime usage.
 *
 * 这是默认的实现，并且可以避免在正常（非工具non-tooled）运行时使用期间向内存中加载过多元数据。
 *
 * @author Rob Harrop
 * @since 2.0
 */
public class NullSourceExtractor implements SourceExtractor {

	/**
	 * This implementation simply returns {@code null} for any input.
	 * 不管输入什么，此实现都只返回null。
	 */
	@Override
	@Nullable
	public Object extractSource(Object sourceCandidate, @Nullable Resource definitionResource) {
		return null;
	}

}

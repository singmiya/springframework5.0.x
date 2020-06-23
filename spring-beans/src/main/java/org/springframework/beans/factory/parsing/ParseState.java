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

package org.springframework.beans.factory.parsing;

import java.util.LinkedList;

import org.springframework.lang.Nullable;

/**
 * Simple {@link LinkedList}-based structure for tracking the logical position during
 * a parsing process. {@link Entry entries} are added to the LinkedList at
 * each point during the parse phase in a reader-specific manner.
 *
 * 在解析处理期间，用来追踪逻辑位置的基于链表（LinkedList）的简单结构。
 * 在解析阶段，以特定读取器（reader-specific）的方式在每一个点将条目加入链表。
 *
 * <p>Calling {@link #toString()} will render a tree-style view of the current logical
 * position in the parse phase. This representation is intended for use in
 * error messages.
 *
 * 在解析阶段，调用#toString()方法将会把当前逻辑位置渲染成一个树形视图。此种表示形式旨在用于错误信息中。
 *
 * @author Rob Harrop
 * @since 2.0
 */
public final class ParseState {

	/**
	 * Tab character used when rendering the tree-style representation.
	 * 当渲染树形表示时使用的Tab字符。
	 */
	private static final char TAB = '\t';

	/**
	 * Internal {@link LinkedList} storage.
	 * 内部链表存储。
	 */
	private final LinkedList<Entry> state;


	/**
	 * Create a new {@code ParseState} with an empty {@link LinkedList}.
	 * 使用空链表创建新的ParseState。
	 */
	public ParseState() {
		this.state = new LinkedList<>();
	}

	/**
	 * Create a new {@code ParseState} whose {@link LinkedList} is a {@link Object#clone clone}
	 * of that of the passed in {@code ParseState}.
	 * 创建一个新的ParseState，它的链表是从传入的ParseState参数中复制而来。
	 */
	@SuppressWarnings("unchecked")
	private ParseState(ParseState other) {
		this.state = (LinkedList<Entry>) other.state.clone();
	}


	/**
	 * Add a new {@link Entry} to the {@link LinkedList}.
	 * 向链表中添加新条目。
	 */
	public void push(Entry entry) {
		this.state.push(entry);
	}

	/**
	 * Remove an {@link Entry} from the {@link LinkedList}.
	 * 从链表中删除一个条目
	 */
	public void pop() {
		this.state.pop();
	}

	/**
	 * Return the {@link Entry} currently at the top of the {@link LinkedList} or
	 * {@code null} if the {@link LinkedList} is empty.
	 * 返回当前在链表头部的条目，如果链表为空则返回null。
	 */
	@Nullable
	public Entry peek() {
		return this.state.peek();
	}

	/**
	 * Create a new instance of {@link ParseState} which is an independent snapshot
	 * of this instance.
	 * 创建一个ParseState的新实例，它是当前实例的一个独立快照。
	 */
	public ParseState snapshot() {
		return new ParseState(this);
	}


	/**
	 * Returns a tree-style representation of the current {@code ParseState}.
	 * 返回当前ParseState的树形表示。
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < this.state.size(); x++) {
			if (x > 0) {
				sb.append('\n');
				for (int y = 0; y < x; y++) {
					sb.append(TAB);
				}
				sb.append("-> ");
			}
			sb.append(this.state.get(x));
		}
		return sb.toString();
	}


	/**
	 * Marker interface for entries into the {@link ParseState}.
	 * 用于进入ParseState的标记接口。
	 */
	public interface Entry {

	}

}

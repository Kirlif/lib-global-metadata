/*
 *  Copyright (C) 2022 github.com/REAndroid
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reandroid.unity.metadata.base;

import com.reandroid.arsc.base.BlockContainer;
import com.reandroid.arsc.container.BlockList;

import java.util.Iterator;

public interface LinkableItem {
    void link();
    default void onIndexLinked(Object linker) {
        linkObject(this);
    }

    static void linkObject(Object block) {
        if (block instanceof LinkableItem) {
            ((LinkableItem) block).link();
        }
    }
    static void linkArray(Object[] blocks) {
        if (blocks != null) {
            int length = blocks.length;
            for (int i = 0; i < length; i++) {
                linkObject(blocks[i]);
            }
        }
    }
    static void linkBlockList(BlockList<?> blockList) {
        int size = blockList.size();
        for (int i = 0; i < size; i++) {
            linkObject(blockList.get(i));
        }
    }
    static void linkBlockContainer(BlockContainer<?> blockContainer) {
        if (!blockContainer.isNull()) {
            linkArray(blockContainer.getChildes());
        }
    }
    static void linkAll(Iterator<?> iterator) {
        while (iterator.hasNext()) {
            linkObject(iterator.next());
        }
    }
}

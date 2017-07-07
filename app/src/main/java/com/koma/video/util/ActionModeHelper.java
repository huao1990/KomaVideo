/*
 * Copyright 2017 Koma
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.koma.video.util;

import android.support.annotation.CallSuper;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.koma.video.base.BaseAdapter;

public class ActionModeHelper implements ActionMode.Callback {
    private static final String TAG = ActionModeHelper.class.getSimpleName();

    @MenuRes
    private int mCabMenu;

    private BaseAdapter mAdapter;

    private ActionMode.Callback mCallback;

    protected ActionMode mActionMode;

    public ActionModeHelper(@NonNull BaseAdapter adapter, @MenuRes int cabMenu) {
        this.mAdapter = adapter;
        this.mCabMenu = cabMenu;
    }

    public ActionModeHelper(@NonNull BaseAdapter adapter, @MenuRes int cabMenu,
                            @Nullable ActionMode.Callback callback) {
        this(adapter, cabMenu);
        this.mCallback = callback;
    }

    public ActionMode getActionMode() {
        return mActionMode;
    }

    /**
     * Implements the basic behavior of a CAB and multi select behavior.
     *
     * @param position the current item position
     * @return true if selection is changed, false if the click event should ignore the ActionMode
     * and continue
     */
    public boolean onClick(int position) {
        if (position != RecyclerView.NO_POSITION) {
            toggleSelection(position);
            return true;
        }
        return false;
    }

    /**
     * Implements the basic behavior of a CAB and multi select behavior onLongClick.
     *
     * @param activity the current Activity
     * @param position the position of the clicked item
     * @return the initialized ActionMode or null if nothing was done
     */
    @NonNull
    public ActionMode onLongClick(AppCompatActivity activity, int position) {
        // Activate ActionMode
        if (mActionMode == null) {
            mActionMode = activity.startSupportActionMode(this);
        }
        // We have to select this on our own as we will consume the event
        if (position != RecyclerView.NO_POSITION) {
            toggleSelection(position);
        }

        return mActionMode;
    }

    /**
     * Toggle the selection state of an item.
     * <p>If the item was the last one in the selection and is unselected, the selection is stopped.
     * Note that the selection must already be started (actionMode must not be null).</p>
     *
     * @param position position of the item to toggle the selection state
     */
    public void toggleSelection(int position) {
        if (position >= 0 && mAdapter.getMode() == BaseAdapter.Mode.MULTI) {
            mAdapter.toggleSelection(position);
        }
        // If SINGLE is active then ActionMode can be null
        if (mActionMode == null) {
            return;
        }

        int count = mAdapter.getSelectedItemCount();

        updateContextTitle(count);
    }

    /**
     * Updates the title of the Context Menu.
     * <p>Override to customize the title and subtitle.</p>
     *
     * @param count the current number of selected items
     */
    public void updateContextTitle(int count) {
        if (mActionMode != null) {
            mActionMode.setTitle(String.valueOf(count));
        }
    }

    @CallSuper
    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        LogUtils.i(TAG, "onCreateActionMode");

        // Inflate the Context Menu
        actionMode.getMenuInflater().inflate(mCabMenu, menu);
        // Activate the ActionMode Multi
        mAdapter.setMode(BaseAdapter.Mode.MULTI);
        // Notify the provided callback
        return mCallback == null || mCallback.onCreateActionMode(actionMode, menu);
    }

    @CallSuper
    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        LogUtils.i(TAG, "onPrepareActionMode");

        return mCallback != null && mCallback.onPrepareActionMode(actionMode, menu);
    }

    @CallSuper
    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem item) {
        LogUtils.i(TAG, "onActionItemClicked item : " + item.getTitle());
        
        boolean consumed = false;

        if (mCallback != null) {
            consumed = mCallback.onActionItemClicked(actionMode, item);
        }
        if (!consumed) {
            // Finish the actionMode
            actionMode.finish();
        }

        return consumed;
    }

    @CallSuper
    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        LogUtils.i(TAG, "onDestroyActionMode");

        // Change mode and deselect everything
        mAdapter.setMode(BaseAdapter.Mode.IDLE);
        mAdapter.clearSelection();

        mActionMode = null;
        // Notify the provided callback
        if (mCallback != null) {
            mCallback.onDestroyActionMode(actionMode);
        }
    }

    /**
     * Utility method to be called from Activity in many occasions such as: <i>onBackPressed</i>,
     * <i>onRefresh</i> for SwipeRefreshLayout, after <i>deleting</i> all selected items.
     *
     * @return true if ActionMode was active (in case it is also terminated), false otherwise
     */
    public boolean destroyActionModeIfCan() {
        if (mActionMode != null) {
            mActionMode.finish();
            return true;
        }
        return false;
    }

}
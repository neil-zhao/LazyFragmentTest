package me.xshen.lazyfragmenttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * 使用继承方式实现的懒加载Fragment基类
 */
public abstract class InheritedFakeFragment extends Fragment {
    protected FrameLayout rootContainer;


    private boolean isLazyViewCreated = false;
    private LayoutInflater inflater;
    private Bundle savedInstanceState;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        this.savedInstanceState = savedInstanceState;
        rootContainer = new FrameLayout(getContext().getApplicationContext());
        rootContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return rootContainer;
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLazyViewCreated && inflater != null) {
            View view = onLazyCreateView(inflater, rootContainer, savedInstanceState);
            rootContainer.addView(view);
            isLazyViewCreated = true;
            onLazyViewCreated(rootContainer, savedInstanceState);
        }
    }

    /**
     * 获取真实的fragment是否已经初始化view
     *
     * @return 已经初始化view返回true，否则返回false
     */
    @SuppressWarnings("unused")
    public boolean isLazyViewCreated() {
        return isLazyViewCreated;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLazyViewCreated = false;
    }

    /**
     * 用于替代真实Fragment的onCreateView，在真正获取到用户焦点后才会调用
     *
     * @param inflater           - The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container          - If non-null, this is the parent view that the fragment's UI should be attached to. The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    protected abstract View onLazyCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState);

    /**
     * 用来代替真实Fragment的onViewCreated，在真正获得用户焦点并且{@link #onLazyViewCreated(View, Bundle)}
     *
     * @param view               - The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     */
    protected abstract void onLazyViewCreated(View view, @Nullable Bundle savedInstanceState);

}

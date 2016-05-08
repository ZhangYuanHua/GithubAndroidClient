package com.frodo.github.business.account;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.frodo.app.android.ui.fragment.StatedFragment;
import com.frodo.app.framework.controller.IModel;
import com.frodo.github.R;
import com.frodo.github.bean.User;
import com.frodo.github.view.CircleProgressDialog;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by frodo on 2016/5/7.
 */
public class ProfileFragment extends StatedFragment<ProfileView, AccountModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public ProfileView createUIView(Context context, LayoutInflater inflater, ViewGroup container) {
        return new ProfileView(this, inflater, container);
    }

    @Override
    protected AccountModel createModel() {
        IModel model = getMainController().getModelFactory().getModelBy(AccountModel.class.getCanonicalName());
        if (model == null) {
            return new AccountModel(getMainController());
        } else {
            return (AccountModel) model;
        }
    }

    @Override
    protected void onFirstTimeLaunched() {
        Bundle bundle = getArguments();
        CircleProgressDialog.showLoadingDialog(getAndroidContext());
        if (bundle != null && bundle.containsKey("username")) {
            loadUserWithReactor(bundle.getString("username"));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_repo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void loadUserWithReactor(final String username) {
        Observable
                .create(new Observable.OnSubscribe<User>() {
                    @Override
                    public void call(Subscriber<? super User> subscriber) {
                        CircleProgressDialog.showLoadingDialog(getAndroidContext());
                        getModel().loadUserWithReactor(username, subscriber);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<User>() {
                            @Override
                            public void call(User user) {
                                CircleProgressDialog.hideLoadingDialog();
                                getUIView().showDetail(user);
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                CircleProgressDialog.hideLoadingDialog();
                            }
                        }
                );
    }
}
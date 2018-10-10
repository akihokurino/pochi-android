package com.wanpaku.pochi.app.launch

import android.content.Intent
import android.os.Bundle
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.internal.BaseActivity
import com.wanpaku.pochi.app.maintab.MainTabActivity
import com.wanpaku.pochi.app.service.UserService
import com.wanpaku.pochi.domain.repositories.PublicAssetRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class LaunchActivity : BaseActivity() {

    @Inject lateinit var publicAssetRepository: PublicAssetRepository
    @Inject lateinit var userService: UserService

    private val disposables: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        component.inject(this)

        disposables.add(publicAssetRepository.setup()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // TODO とりあえず
                    if (userService.isLoggedIn()) {
                        MainTabActivity.startActivity(this)
                    } else {
                        val intent = Intent(this, WelcomeActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                }, {
                    //TODO リトライダイアログ等出す
                }))
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}

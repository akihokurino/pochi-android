package com.wanpaku.pochi.app.sitterdetail

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.wanpaku.pochi.R
import com.wanpaku.pochi.app.delegate.ToolbarDelegate
import com.wanpaku.pochi.app.internal.BaseActivity
import com.wanpaku.pochi.domain.sitter.Sitter
import com.wanpaku.pochi.infra.ui.widget.load

class SitterDetailActivity : BaseActivity() {

    companion object {

        private val KEY_SITTER = "key_sitter"

        fun startActivity(activity: BaseActivity, sitter: Sitter) {
            val intent = Intent(activity, SitterDetailActivity::class.java)
                    .apply { putExtra(KEY_SITTER, sitter) }
            activity.startActivity(intent)
        }

        private fun sitter(intent: Intent): Sitter = intent.getParcelableExtra(KEY_SITTER)

    }

    override fun lifecycleDelegates() = listOf(
            ToolbarDelegate(displayHomeAsUpEnabled = true, titleEnabled = false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sitter_detail)
        setupMainThumbnail()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SitterDetailFragment.newInstance(sitter(intent)), SitterDetailFragment.TAG)
                    .commit()
        }
    }

    private fun setupMainThumbnail() {
        val mainThumbnail = findViewById<ImageView>(R.id.main_thumbnail)
        sitter(intent).mainImage?.let { mainThumbnail.load(it) }
    }

}
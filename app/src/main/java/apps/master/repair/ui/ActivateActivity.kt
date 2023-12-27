package apps.master.repair.ui

import android.content.Intent
import android.os.Bundle
import apps.master.repair.R
import apps.master.repair.databinding.ActivityActivateBinding
import nearby.lib.mvvm.activity.BaseAppBindActivity


class ActivateActivity : BaseAppBindActivity<ActivityActivateBinding>() {

    override fun layoutRes(): Int {
        return R.layout.activity_activate
    }

    override fun initialize(savedInstanceState: Bundle?) {
        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishPage(ActivateActivity@this)
        }
    }

}
package apps.master.repair.ui

import android.os.Bundle
import apps.master.repair.R
import apps.master.repair.databinding.ActivityLauncherBinding
import apps.master.repair.http.IndexViewModel
import nearby.lib.mvvm.activity.BaseAppBVMActivity


class LauncherActivity : BaseAppBVMActivity<ActivityLauncherBinding, IndexViewModel>() {
    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }


    override fun layoutRes(): Int {
        return R.layout.activity_launcher
    }


    override fun initialize(savedInstanceState: Bundle?) {
        navigate(ActivateActivity::class.java)
        finishPage(LauncherActivity@ this)
//        viewModel.start()
//        viewModel.start.observe(this) {
//            if (it) {
//                navigate(ActivateActivity::class.java)
//                finishPage(LauncherActivity@ this)
//            }
//        }
        intent?.let {
            println("MessagingService intent")
            println("MessagingService ${it.getStringExtra("key1")} - ${it.getStringExtra("key2")}")
        }
    }

}
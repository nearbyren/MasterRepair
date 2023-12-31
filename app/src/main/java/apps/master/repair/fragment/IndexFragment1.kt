package apps.master.repair.fragment

import android.os.Bundle
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import apps.master.repair.R
import apps.master.repair.databinding.FragmentIndex1Binding
import apps.master.repair.dialog.RequestCompleteDialogFragment
import apps.master.repair.http.IndexViewModel
import apps.master.repair.model.ServiceDto
import apps.master.repair.uitl.ConstantUtil
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import nearby.lib.base.bar.BarHelperConfig
import nearby.lib.base.exts.observeNonNull
import nearby.lib.base.uitl.SPreUtil
import nearby.lib.mvvm.fragment.BaseAppBVMFragment
import nearby.lib.signal.livebus.LiveBus
import nearby.lib.uikit.recyclerview.BaseRecyclerAdapter
import nearby.lib.uikit.recyclerview.SpaceItemDecoration
import nearby.lib.uikit.widgets.dpToPx


/**
 * @author: lr
 * @created on: 2023/12/18 9:13 PM
 * @description:
 */
class IndexFragment1 :
    BaseAppBVMFragment<FragmentIndex1Binding, IndexViewModel>() {
    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_index_1
    }
    private fun toNetwork() {
        //請求維修列表
        val id = SPreUtil[requireActivity(), "id", 1]
        viewModel.staff(id.toString())
    }
    private var activityItems = mutableListOf<ServiceDto>()
    private val indexTagAdapter by lazy { apps.master.repair.adapter.ItemServiceStatusAdapter() }
    private var layoutManager: LinearLayoutManager? = null
    private var isRefresh = false
    var requestDialog: RequestCompleteDialogFragment? = null
    override fun initialize(savedInstanceState: Bundle?) {

        viewModel.serviceDtos.observeNonNull(this) {
            // 这里可以异步请求数据，刷新完成后调用 refreshLayout.finishRefresh()
            binding.srl.finishRefresh()
            if (isRefresh) activityItems.clear()
            if (it.size == 0) {
                return@observeNonNull
            }
            activityItems.addAll(it)
            indexTagAdapter.notifyDataSetChanged()
        }
        initRefresh()
        toNetwork()
        LiveBus.get("confirm").observe(this) {
            println("刷新列表")
            requestDialog?.let { request ->
                request.dismiss()
            }
            toNetwork()
        }

    }
    private fun initRefresh() {
        layoutManager = LinearLayoutManager(context)
        indexTagAdapter.setItems(activityItems)
        binding.recycle.adapter = indexTagAdapter
        binding.recycle.layoutManager = layoutManager
        binding.recycle.addItemDecoration(SpaceItemDecoration(12.dpToPx, 0.dpToPx, 0.dpToPx))
        binding.recycle.setHasFixedSize(true)
        binding.recycle.itemAnimator = null
        indexTagAdapter.setOnItemClickListener(listener = object :
            BaseRecyclerAdapter.OnItemClickListener<ServiceDto> {
            override fun onItemClick(holder: Any, item: ServiceDto, position: Int) {
                requestDialog = RequestCompleteDialogFragment()
                requestDialog?.let { request->
                    val bundle = Bundle()
                    bundle.putString("numbers", item.numbers)
                    request.arguments = bundle
                    request.setGravity(Gravity.BOTTOM)
                    request.show(this@IndexFragment1)
                }

            }
        })

        binding.srl.setRefreshHeader(ClassicsHeader(requireActivity()))
        binding.srl.setRefreshFooter(ClassicsFooter(requireActivity()))
        //设置 Footer 为 球脉冲 样式
//        binding.srl.setRefreshFooter(BallPulseFooter(requireActivity()).setSpinnerStyle(SpinnerStyle.Scale));

        binding.srl.setOnRefreshListener(OnRefreshListener { refreshlayout ->
            isRefresh = true
            toNetwork()
        })


        // 设置上拉加载更多监听
        binding.srl.setOnLoadMoreListener { refreshLayout -> // 执行加载更多操作
            // 这里可以异步请求数据，加载完成后调用 refreshLayout.finishLoadMore()
            binding.srl.finishLoadMore()
        }
    }



    override fun initBarHelperConfig(): BarHelperConfig {
        return BarHelperConfig.builder().setBack(false)
            .setBgColor(nearby.lib.base.R.color.dodgerblue)
            .setTitle(title = getString(R.string.menu_01), titleColor = nearby.lib.base.R.color.white).build()
    }
}
package apps.master.repair.adapter

import androidx.core.content.ContextCompat
import apps.master.repair.R
import apps.master.repair.databinding.ItemServiceStatusBinding
import apps.master.repair.model.ServiceDto
import apps.master.repair.uitl.ConstantUtil
import nearby.lib.uikit.recyclerview.BaseBindRecyclerAdapter


class ItemServiceStatusAdapter : BaseBindRecyclerAdapter<ItemServiceStatusBinding, ServiceDto>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_service_status
    }

    override fun onBindingItem(binding: ItemServiceStatusBinding, item: ServiceDto, position: Int) {
        binding.address.text = item.schoolName
        binding.addressNo.text = "${item.schoolNo} position"
        binding.addressStatus.text = item.statusText
        when (item.status) {
            ConstantUtil.SERVICE_STATUS_QUOTE -> {
                val img = ContextCompat.getDrawable(context, R.drawable.index_status_shape_1)
                binding.addressStatus.setCompoundDrawables(img, null, null, null)
                binding.addressStatus.setTextColor(ContextCompat.getColor(context,R.color.item_status_1))


            }

            ConstantUtil.SERVICE_STATUS_QUOTED -> {
                val img = ContextCompat.getDrawable(context, R.drawable.index_status_shape_2)
                binding.addressStatus.setCompoundDrawables(img, null, null, null)
                binding.addressStatus.setTextColor(ContextCompat.getColor(context,R.color.item_status_2))
            }

            ConstantUtil.SERVICE_STATUS_CONFIRM -> {
                val img = ContextCompat.getDrawable(context, R.drawable.index_status_shape_3)
                binding.addressStatus.setCompoundDrawables(img, null, null, null)
                binding.addressStatus.setTextColor(ContextCompat.getColor(context,R.color.item_status_3))
            }

            ConstantUtil.SERVICE_STATUS_FINISH -> {
                val img = ContextCompat.getDrawable(context, R.drawable.index_status_shape_4)
                binding.addressStatus.setCompoundDrawables(img, null, null, null)
                binding.addressStatus.setTextColor(ContextCompat.getColor(context,R.color.item_status_4))
            }
        }
    }
}
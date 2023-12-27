package apps.master.repair.adapter


import apps.master.repair.R
import apps.master.repair.databinding.ItemShowAlbumBinding
import apps.master.repair.model.AlbumDto
import com.bumptech.glide.Glide
import nearby.lib.uikit.recyclerview.BaseBindRecyclerAdapter


class ItemShowAlbumAdapter : BaseBindRecyclerAdapter<ItemShowAlbumBinding, AlbumDto>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_show_album
    }

    override fun onBindingItem(
        binding: ItemShowAlbumBinding, item: AlbumDto, position: Int
    ) {
        Glide.with(context).load(R.drawable.weizhi).into(binding.imgBg)
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

}
package apps.master.repair.dialog

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import apps.master.repair.R
import apps.master.repair.adapter.ItemShowAlbumAdapter
import apps.master.repair.adapter.ItemAddAlbumAdapter
import apps.master.repair.databinding.FragmentRequestCompleteBinding
import apps.master.repair.http.IndexViewModel
import apps.master.repair.model.AlbumDto
import apps.master.repair.model.InventoryDto
import apps.master.repair.uitl.ConstantUtil.ALBUM_REQUEST_CODE
import com.bumptech.glide.Glide
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import com.zhihu.matisse.internal.entity.CaptureStrategy
import nearby.lib.base.exts.observeNonNull
import nearby.lib.base.uitl.SPreUtil
import nearby.lib.base.uitl.ToastUtils
import nearby.lib.mvvm.fragment.BaseAppBVMDialogFragment
import nearby.lib.uikit.recyclerview.BaseRecyclerAdapter
import nearby.lib.uikit.recyclerview.SpaceItemDecoration
import nearby.lib.uikit.widgets.dpToPx
import java.io.File

/*維修完成提交*/
class RequestCompleteDialogFragment :
    BaseAppBVMDialogFragment<FragmentRequestCompleteBinding, IndexViewModel>() {
    //維修位置
    private var locationImage = StringBuilder()
    override fun createViewModel(): IndexViewModel {
        return IndexViewModel()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_request_complete
    }

    override fun initialize(savedInstanceState: Bundle?) {
        binding.iconBack.setOnClickListener { dismiss() }
        viewModel.inventoryDto.observeNonNull(this) {
            it?.let {
                toUI(it)
            }
        }

    }

    private fun toUI(inventorys: InventoryDto) {
        //会员用户位置图
        Glide.with(requireActivity()).load(inventorys.addressImage).into(binding.image)
        //会员位置地址
        binding.address2.text = inventorys.repairAddress
        //需要維修的地點
        binding.repairsLocationsText.text = inventorys.describes
        //緊急程度
        binding.urgentLevelText.text = ""
        //聯絡人姓名
        binding.contactPersonNameText.text = ""
        //電話
        binding.telephoneText.text = ""
        //請描述詳情
        binding.detailText.text = ""
        //地點照片
        Glide.with(requireActivity()).load(inventorys.addressImage).into(binding.ivRepairsAddress)
        //維修中位置圖
        inMaintenance()
        //維修完畢位置圖
        inFinish()
        //提交上傳
        binding.confirm.setOnClickListener {
            //維修位置圖
            if (albums.size == 0) {
                ToastUtils.showToast("請上傳維完工图")
                return@setOnClickListener
            }
            val size = albums.size
            for (i in 0 until size) {
                val t = size - 1
                println("我来了 $t ")
                if (albums.size > 1 && i < t) {
                    locationImage.append("${albums[i]},")
                } else {
                    locationImage.append("${albums[i]}")
                }
            }
            submit()
        }


    }
    private fun inMaintenance(){
        //需要維修位置圖
        albumDtos.add(AlbumDto("水喉渠務", R.drawable.img_del))
        albumDtos.add(AlbumDto("防漏防水", R.drawable.img_del))
        albumDtos.add(AlbumDto("門窗", R.drawable.img_del))
        albumDtos.add(AlbumDto("木工", R.drawable.img_del))
        albumDtos.add(AlbumDto("廢物處理", R.drawable.img_del))
        albumDtos.add(AlbumDto("冷氣", R.drawable.img_del))
        itemShowAlbumAdapter.setItems(albumDtos)
        binding.recycleRepairsPicture.adapter = itemShowAlbumAdapter
        binding.recycleRepairsPicture.layoutManager = GridLayoutManager(context, 3)
        binding.recycleRepairsPicture.addItemDecoration(SpaceItemDecoration(0, 10.dpToPx, 10.dpToPx))
        binding.recycleRepairsPicture.setHasFixedSize(true)
        binding.recycleRepairsPicture.itemAnimator = null
        itemShowAlbumAdapter.setOnItemClickListener(listener = object :
            BaseRecyclerAdapter.OnItemClickListener<AlbumDto> {
            override fun onItemClick(holder: Any, item: AlbumDto, position: Int) {
            }
        })
    }
    private fun inFinish(){
        binding.recycleCompleted.adapter = itemAlbumAdapter
        binding.recycleCompleted.layoutManager = GridLayoutManager(context, 3)
        binding.recycleCompleted.addItemDecoration(SpaceItemDecoration(0, 10.dpToPx, 10.dpToPx))
        binding.recycleCompleted.setHasFixedSize(true)
        binding.recycleCompleted.itemAnimator = null

        itemAlbumAdapter.setOnItemClickListener(object :
            apps.master.repair.adapter.ItemClickListener {
            override fun onItemClick(obj: Any, position: Int) {
                Log.e("position", "position---->$position")
                if (albums.size === position && mMaxNumber - albums.size > 0) {
                    //动态申请权限
                    val size = mMaxNumber - albums.size
                    goAlbum(size)
                } else {
                    //跳转至删除或者预览页面
                }
            }
        })
        itemAlbumAdapter.setOnDeleteClickListener(object :
            apps.master.repair.adapter.DeleteListener {
            override fun onDeleteClick(obj: Any, position: Int) {
                removeList(position)
            }
        })
    }

    private fun submit() {
        val id = SPreUtil[requireActivity(), "id", 1] as Int

    }

    private val itemAlbumAdapter by lazy {
        ItemAddAlbumAdapter(
            requireActivity(),
            albums,
            mMaxNumber
        )
    }
    private var albums = mutableListOf<String>()
    private var albumDtos = mutableListOf<AlbumDto>()
    private val itemShowAlbumAdapter by lazy { ItemShowAlbumAdapter() }
    private var mMaxNumber: Int = 5

    /**
     * 删除图片
     *
     * @param position
     */
    private fun removeList(position: Int) {
        albums.removeAt(position)
        itemAlbumAdapter.nodfiyData(albums)
    }

    private fun goAlbum(selectable: Int) {
        Matisse.from(this)
            .choose(MimeType.ofImage())
            .countable(true)
            .maxSelectable(selectable)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .showPreview(false) // Default is `true`
            .capture(true)
            .captureStrategy(CaptureStrategy(true, "PhotoPicker"))
            .forResult(ALBUM_REQUEST_CODE)
    }

    private fun upload(path: String) {
        viewModel.upload(File(path))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ALBUM_REQUEST_CODE -> {
                data?.let {
                    val pathList = Matisse.obtainPathResult(it)
                    for (i in 0 until pathList.size) {
                        val path = pathList[i]
                        println("图片${(i + 1)} 地址 $path")
                        upload(path)
                    }
                    itemAlbumAdapter.nodfiyData(albums)
                }

            }
        }
    }
}
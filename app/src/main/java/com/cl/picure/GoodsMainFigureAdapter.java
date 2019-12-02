package com.cl.picure;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.cl.picure.MainActivity.TAG_NEGATIVE;

/**
 * 项目：hbtGit
 * 版权：蒲公英公司 版权所有
 * 作者：Arry
 * 版本：1.0
 * 创建日期：2019-11-08
 * 描述：
 * 修订历史：
 */
public class GoodsMainFigureAdapter extends RecyclerView.Adapter<GoodsMainFigureAdapter.SelectedPicViewHolder>{

    private int maxImgCount;
    private Context mContext;
    private List<String> mData = new ArrayList<>();

    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener listener;
    private boolean isAdded;   //是否额外添加了最后一个图片

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void setImages(List<String> data) {
        mData = new ArrayList<>(data);
        if (getItemCount() < maxImgCount) {
            mData.add(new String());
            isAdded = true;
        } else {
            isAdded = false;
        }
        notifyDataSetChanged();
    }

    public List<String> getImages() {
        //由于图片未选满时，最后一张显示添加图片，因此这个方法返回真正的已选图片
        if (isAdded) return new ArrayList<>(mData.subList(0, mData.size() - 1));
        else return mData;
    }

    public GoodsMainFigureAdapter(Context mContext, List<String> data, int maxImgCount) {
        this.mContext = mContext;
        this.maxImgCount = maxImgCount;
        this.mInflater = LayoutInflater.from(mContext);
        setImages(data);
    }

    @Override
    public SelectedPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectedPicViewHolder(mInflater.inflate(R.layout.item_list_image, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectedPicViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SelectedPicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView mIvDelete;
        private ImageView iv_img;
        private int clickPosition;

        public SelectedPicViewHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_img);
            mIvDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }

        public void bind(final int position) {
            //设置条目的点击事件
            itemView.setOnClickListener(this);
            //根据条目位置设置图片
            String item = mData.get(position);
            if (isAdded && position == getItemCount() - 1) {
                iv_img.setImageResource(R.mipmap.ic_add_pic);
                clickPosition = TAG_NEGATIVE;
                mIvDelete.setVisibility(View.GONE);
            } else {
                Glide.with(mContext).load(item).into(iv_img);
                clickPosition = position;
                mIvDelete.setVisibility(View.VISIBLE);
            }
            mIvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(mData!=null){
//                        remove(position);
//                    }
                    if (onImage != null) {
                        onImage.OnItemImg(position);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, clickPosition);
        }
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 监听器
     */
    public interface OnImage {
        public void OnItemImg(int position);
    }

    public OnImage onImage;

    public void setOnImage(OnImage onImage) {
        this.onImage = onImage;
    }

}

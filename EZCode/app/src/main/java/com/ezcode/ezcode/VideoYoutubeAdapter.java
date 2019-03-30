package com.ezcode.ezcode;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcode.ezcode.R;
import com.ezcode.ezcode.VideoYoutube;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoYoutubeAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<VideoYoutube> videoList;

    public VideoYoutubeAdapter(Context context, int layout, List<VideoYoutube> videoList) {
        this.context = context;
        this.layout = layout;
        this.videoList = videoList;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    public List<VideoYoutube> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoYoutube> videoList) {
        this.videoList = videoList;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    private class ViewHolder{
        ImageView imgThumb;
        TextView txtTitle;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.textViewTitle);
            viewHolder.imgThumb = (ImageView)convertView.findViewById(R.id.imageViewThumb);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        VideoYoutube video = videoList.get(position);
        viewHolder.txtTitle.setText(video.getTitle());
        Picasso.with(context).load(video.getThumbnail()).into(viewHolder.imgThumb);
        return convertView;
    }

}

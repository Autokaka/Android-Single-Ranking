package cn.dshitpie.dataanalyser;

import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CardViewHolder> {

    //MyAdapter的成员变量CardInfoList, 这里被我们用作数据的来源
    private List<CardInfo> cardInfoList;

    //MyAdapter的构造器
    public MyRecyclerViewAdapter(List<CardInfo> cardInfoList) {
        this.cardInfoList = cardInfoList;
    }

    //重写3个抽象方法
    //onCreateViewHolder()方法 返回我们自定义的 ContactViewHolder对象
    @Override
    public CardViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return (new CardViewHolder(itemView));
    }

    @Override
    public void onBindViewHolder (CardViewHolder holder, int position) {

        //cardInfoList中包含的都是cardInfoList类的对象
        //通过其get()方法可以获得其中的对象
        CardInfo cardInfo = cardInfoList.get(position);

        //将viewholder中hold住的各个view与数据源进行绑定(bind)
        holder.vTitle.setText(CardInfo.TITLE + ": " + cardInfo.title);
        holder.vCode.setText(CardInfo.CODE + ": " + cardInfo.code);
        holder.vCardReview.setImageBitmap(BitmapFactory.decodeFile(cardInfo.imgPath));
    }

    //此方法返回列表项的数目
    @Override
    public int getItemCount() {
        return cardInfoList.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        //create the viewHolder class
        protected TextView vTitle;
        protected TextView vCode;
        protected ImageView vCardReview;

        public CardViewHolder(View itemView) {
            super(itemView);
            vTitle = (TextView) itemView.findViewById(R.id.title);
            vCode = (TextView) itemView.findViewById(R.id.code);
            vCardReview = (ImageView) itemView.findViewById(R.id.card_img);
        }
    }
}

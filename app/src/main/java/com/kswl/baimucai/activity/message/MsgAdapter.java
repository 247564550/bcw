package com.kswl.baimucai.activity.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseConversationList;
import com.hyphenate.util.DateUtils;
import com.kswl.baimucai.R;
import com.kswl.baimucai.activity.base.IBaseAdapter;
import com.kswl.baimucai.activity.base.ViewHolder;

import java.util.Date;
import java.util.List;

/**
 * @author wangjie
 * @package com.kswl.baimucai.activity.main
 * @desc 商品适配器
 * @date 2017-2017/1/19-15:20
 */
public class MsgAdapter extends IBaseAdapter<EMConversation> {

    private EaseConversationList.EaseConversationListHelper cvsListHelper;

    public MsgAdapter(Context mContext, List<EMConversation> data) {
        super(mContext, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_msg, null);
        }
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_content = ViewHolder.get(convertView, R.id.tv_content);
        TextView tv_count = ViewHolder.get(convertView, R.id.tv_count);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
        ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_icon);
        ImageView iv_state = ViewHolder.get(convertView, R.id.iv_state);

        EMConversation conversation = getItem(position);
        // get username or group id
        String username = conversation.conversationId();

        EaseUserUtils.setUserAvatar(mContext, username, iv_icon);
        EaseUserUtils.setUserNick(username, tv_name);

        if (conversation.getUnreadMsgCount() > 0) {
            // show unread message count
            tv_count.setText(String.valueOf(conversation.getUnreadMsgCount()));
            tv_count.setVisibility(View.VISIBLE);
        } else {
            tv_count.setVisibility(View.INVISIBLE);
        }

        if (conversation.getAllMsgCount() != 0) {
            // show the content of latest message
            EMMessage lastMessage = conversation.getLastMessage();
            String content = null;
            if (cvsListHelper != null) {
                content = cvsListHelper.onSetItemSecondaryText(lastMessage);
            }
            tv_content.setText(EaseSmileUtils.getSmiledText(mContext, EaseCommonUtils
                    .getMessageDigest(lastMessage, mContext)), TextView.BufferType.SPANNABLE);
            if (content != null) {
                tv_content.setText(content);
            }
            tv_time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() ==
                    EMMessage.Status.FAIL) {
                iv_state.setVisibility(View.VISIBLE);
            } else {
                iv_state.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}

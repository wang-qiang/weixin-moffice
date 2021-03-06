package com.dinglan.moffice;

import java.util.List;

import com.dinglan.moffice.model.Share;
import com.dinglan.moffice.model.Stimulate;
import com.dinglan.moffice.model.User;
import com.dinglan.weixin.api.ApiResult;
import com.dinglan.weixin.api.MessageApi;
import com.dinglan.weixin.api.UserApi;
import com.dinglan.weixin.msg.JsonTextMsg;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;

public class ShareController extends Controller{
	
	public void index() {
		Page<Share> list =Share.me.paginateImage(1,20);
		this.setAttr("shares", list.getList().toArray());
		this.render("/share/index.html");
	}
	
	public void imageList(){
		Page<Share> list =Share.me.paginateImage(1,20);
		this.setAttr("list", list.getList().toArray());
		this.render("/share/imageList.html");
	}
	/*
	 * 获取当前用户信息
	 */
	public void getUserByCode(){
		ApiResult codes=UserApi.getUserByCode("10", this.getPara("code"));
		User user = User.me.findByPhone(codes.get("UserId"));
		if(user==null)
			this.renderJson("{}");
		else
			this.renderJson(user);
	}
	/*
	 * 留言 
	 */
	public void chat() {
		ApiResult ret = UserApi.getFollows(1);
		this.setAttr("users",ret.getList("userlist"));
		this.render("/share/chat.html");
	}
	/*
	 * 发送消息
	 */
	public void sendMsg() {
		
		String username= this.getPara("username");
		String content= this.getPara("content");
		String touser= this.getPara("touser");
		if(touser=="all") touser="@all";
		
		if(touser.length()==0 || content.length()==0){
			this.renderJson("{'errcode':'1','errmsg':'信息不完整1'}");
		}else{
			JsonTextMsg msg=new JsonTextMsg("10","0"); 
			msg.touser=touser;
			msg.content=username+":"+content;
			ApiResult ret= MessageApi.sendMsg(msg);
			this.renderJson(ret.getJson());
		}
	}
	/*
	 * 取得公告
	 */
	public void newList() {
		List<Stimulate> list =Stimulate.me.list(1,10);
		this.setAttr("list", list);
		this.render("/share/newList.html");
	}
	/*
	 * 取得公告
	 */
	public void getNew() {
		String id=this.getPara("id");
		Stimulate model =Stimulate.me.findById(id);
		this.setAttr("new", model);
		this.render("/share/new.html");
	}
	/*
	 * 签到列表
	 */
	public void gpsList() {
		Page<Share> shares =Share.me.paginateGps(1, 50);
		this.setAttr("shares", shares.getList());
		this.render("/share/gpsList.html");
	}
	/*
	 * 留言列表
	 */
	public void textList() {
		Page<Share> shares =Share.me.paginateText(1, 50);
		this.setAttr("shares", shares.getList());
		this.render("/share/textList.html");
	}
}

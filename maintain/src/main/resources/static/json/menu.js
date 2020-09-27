{
	"code": 0,
	"msg": "",
	"data": [
		{
			"name": "adminUser",
			"title": "后台用户管理(Demo)",
			"icon": "layui-icon-app",
			"jump": "adminUser/list"
		},
		{
			"name": "userCenter",
			"title": "用户中心",
			"icon": "layui-icon-app",
			"spread" : false,
			"list" : [
				{
					"name": "updatePwd",
					"title": "修改密码",
					"icon": "layui-icon-group",
					"jump": "userCenter/updatePwd"
				}
			]
		}
	]
}
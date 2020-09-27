/**
 * 公共的js，我这里基本上把所有常用的都写在了这里
 */
layui.extend({
	notice: '{/}layui/exts/notice/notice' //{/}表示不继承base的设置
  }).define(['table', 'form', 'element', 'upload', 'laydate', 'notice', 'toastr'], function(exports){

  //图片访问根目录
  var OSSBasePath = 'http://boyun-test.oss-cn-beijing.aliyuncs.com/';
  if(location.href.indexOf('.com') == -1) { //开发环境
	OSSBasePath = 'http://boyun-test.oss-cn-beijing.aliyuncs.com/';
  }
  //私有文件访问目录
  var PrivateBasePath = 'getFile/';



  //laydate的默认格式化时间
  var LaydateFormat = 'yyyy/MM/dd';
  var LaydatetimeFormat = 'yyyy/MM/dd HH:mm:ss';
  var LaydatetimeminuteFormat = 'yyyy/MM/dd HH:mm';

  //上传参数限制
  var UploadImageMaxSize = 1024 * 2;  //2M
  var UploadVideoMaxSize = 1024 * 5;  //5M

  //公共变量和方法
  $ = layui.$;
  layer = layui.layer;
  laytpl = layui.laytpl;
  setter = layui.setter;
  view = layui.view;
  form = layui.form;
  table = layui.table;
  admin = layui.admin;
  element = layui.element;
  notice = layui.notice;
  
  //拼接图片路径
  imageUrl = function(imageId){
	  return fileUrl(imageId);
  }

  //拼接视频路径
  videoUrl = function(videoId){
	return fileUrl(videoId);
  }

  //拼接文件路径
  fileUrl = function(fileId) {
	var url = '';
	if(fileId.indexOf('private') != -1) {
		url = PrivateBasePath + fileId;
	}else {
		url = OSSBasePath + fileId;
	}
  	return url;
  }

  //把图片路径或id包裹成一个自适应的<img/>标签
  wrapImage = function (url){
  	  if(url.indexOf('http') == -1) {
		 url = imageUrl(url);
	  }
	  return '<img width="100%" class="form-upload-preview-image" lay-tips="点击看大图" src="'+ url +'"/>'
  }

  //layui.table列的模板的默认值，做了一些默认处理
  templetDef = function(field, func){
  	  return function(d){
  	  	  if(hasText(d[field])) {
  	  	  	if(func != null) {
                return func(d[field], d);
			}else {
  	  	  		return d[field];
			}
		  }else {
  	  	  	return '-';
		  }
	  }
  }
  
  renderDate = function(option) {
	option = $.extend({
	  elem : '#timeRange',
	  type : 'date',
	  range : true,
	  trigger : 'click',
	  format : LaydateFormat,
	  min : 0,
	}, option);
	//日期时间范围
	layui.laydate.render(option);
  }
  
  renderDatetime = function(option) {
	  option = $.extend({
		  elem : '#timeRange',
		  type : 'datetime',
		  range : true,
		  trigger : 'click',
		  format : LaydatetimeFormat,
		  min : 0,
		}, option);
		//日期时间范围
		layui.laydate.render(option);
  }
  
  renderDatetimeminute = function(option){
	  option = $.extend({
		  elem : '#timeRange',
		  type : 'datetime',
		  range : true,
		  trigger : 'click',
		  format : LaydatetimeminuteFormat,
		  min : 0,
		}, option);
		//日期时间范围
		layui.laydate.render(option);
  }

  String.prototype.replaceAll  = function(s1,s2){
    return this.replace(new RegExp(s1,"gm"),s2);
  };
  
  //公共业务的逻辑处理可以写在此处，切换任何页面都会执行
  //注意，变量的声明一定不要写var，要是全局的别的页面才能获取到
  //自动渲染面包屑导航
  renderBreadcrumb = function(){
	if(!$('.layadmin-header')[0]) return;
	var $this = $('.layui-side-menu .layui-this');
	var grandfather = $this.parents('li').children('a').find('cite').text();
	var father = $this.closest('.layui-nav-itemed').children('a').text();
	var cur = $this.text();
	var html = [];
	
	//用于判断当前菜单的层级
	var dlSize = $this.parents('dl').size();
	
	html.push('<div class="layui-breadcrumb" lay-filter="breadcrumb">');
//	html.push('<a lay-href="">主页</a>');
	if(grandfather != '') {
		html.push('<a><cite>'+ grandfather +'</cite></a>');
	}
	//只有在第三级的菜单才有第二级菜单
	if(dlSize == 2) {
		html.push('<a><cite>'+ father +'</cite></a>');
	}
	html.push('<a><cite>'+ cur +'</cite></a>');
	html.push('</div>');
	$('.layadmin-header').prepend(html.join(''));
	
	//自动生成的面包屑导航，需要重新渲染
	element.render('breadcrumb')
  }
  
  //渲染返回按钮
  renderBack = function(){
	  //$('#LAY-BACK').show();
  }
  
  //cha在str中第num出现的索引位置
  function strFind(str,cha,num){
    var x=str.indexOf(cha);
    for(var i=0;i<num;i++){
        x=str.indexOf(cha,x+1);
    }
    return x;
  }
  
  //添加页面的前缀
  wrapPagePrefix = function(pageName) {
	var hash = location.hash;
	var prefix = hash.substring(hash.indexOf('#') + 2, hash.lastIndexOf('/') + 1);
	return prefix + pageName; 
  }
  
  //添加接口的前缀
  wrapApiPrefix = function(apiName){
	  return 'api/' + apiName;
  }
  
  //列表页面的封装
  PageList = function(globalOptions){
	  globalOptions = $.extend({
		  module : 'user', //模块，api的地址的一部分
	  }, globalOptions);

	  this.globalOptions = globalOptions;
	  
	  //公共命名，不能重复
	  var LayTable = globalOptions.LayTable ? globalOptions.LayTable : 'LAY-TABLE';
	  var LaySearchBtn = globalOptions.LaySearchBtn ? globalOptions.LaySearchBtn : 'LAY-SEARCH-BTN';
	  
	  this.LayTable = LayTable;
	  
	  //常用参数
	  var module = globalOptions.module;
	  var self = this;
	  
	  //渲染表格
	  this.table = function(options){
		  options = $.extend({
		    elem: '#' + LayTable
		    ,url: 'api/'+ globalOptions.module +'/getByPage'
		    ,unresize : true
		    ,cols: [[
		      {type: 'checkbox', fixed: 'left'}
		      ,{field: 'id', width: 100, title: '文章ID', sort: true}
		      ,{field: 'label', title: '文章标签', minWidth: 100}
		      ,{field: 'title', title: '文章标题'}
		      ,{field: 'author', title: '作者'}
		      ,{field: 'uploadtime', title: '上传时间', sort: true}
		      ,{field: 'status', title: '发布状态', templet: '#buttonTpl', minWidth: 80, align: 'center'}
		      ,{title: '操作', minWidth: 150, align: 'center', fixed: 'right', toolbar: '#table-content-list'}
		    ]],
		    done : function(){
		    	table.resize(LayTable);
		    },
		  	request : {
			    pageName : 'page',
			    limitName : 'pageSize',	
			},
			response: {
			    statusName: 'status', //数据状态的字段名称，默认：code
			    statusCode: 1, //成功的状态码，默认：0
			    msgName: 'msg', //状态信息的字段名称，默认：msg
			    countName: 'totalRows', //数据总数的字段名称，默认：count
			    dataName: 'data' //数据列表的字段名称，默认：data
			},
		    page: true,
		    limit: 10,
		    limits: [10, 30, 60, 90, 150, 300],
		    text: '数据加载异常，请刷新重试。'
		  }, options);
		
		  
		if(options.initSort != null) {
			if(options.where == null) {
				options.where = {};
			}
			options.where.order = options.initSort.field;
			options.where.sort = options.initSort.type;
		}

		//给列设置默认的属性
		options.cols[0].forEach(function(col, i){
			if(col.usedef == null) {
				col.usedef = true;
			}
			if(col.usedef) {
				col.align = 'center';
				col.unresize = true;
			}
		})

		//执行渲染
		table.render(options);
		
		//排序
		table.on('sort('+ LayTable +')', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
		  //尽管我们的 table 自带排序功能，但并没有请求服务端。
		  //有些时候，你可能需要根据当前排序的字段，重新向服务端发送请求，如：
		  table.reload(LayTable, {
		    initSort: obj, //记录初始排序，如果不设的话，将无法标记表头的排序状态。 layui 2.1.1 新增参数
		    where: { //请求参数
		      order: obj.field, //排序字段
		      sort: obj.type //排序方式
		    }
		  });
		  return false;
		});
	  };
	  
	  //搜索
	  this.search = function(callback){
		//监听搜索
		form.on('submit('+ LaySearchBtn +')', function(data){
		  var field = data.field;
		  if(hasText(field.timeRange)) {
			  var arr = field.timeRange.split(' - ');
			  field.startDate = str2ms(arr[0]);
			  field.endDate = str2ms(arr[1]);
		  }else {
			  field.startDate = 0;
			  field.endDate = 0;
		  }
		  if(callback != null) {
			  callback(field);
		  }
		  /*
		  注意，reload方法只会在原来的搜索条件的基础上追加而不会整个
		  覆盖
		   */
		  table.reload(LayTable, {
		    where: field,
			  page : {
		    	curr : 1
			  }
		  });
		});
		form.render();
	  }
	  
	  //刷新
	  this.refresh = function(options){
	  	  if($('.layui-laypage-btn').size() != 0) {
			  $('.layui-laypage-btn').click();
		  }else {
			  table.reload(LayTable, options);
		  }
	  }

	  //获取选中的行的id，返回id是数组
	  this.getCheckedIds = function(){
          var checkStatus = table.checkStatus(self.LayTable);
          var data = checkStatus.data; //得到选中的数据
          if(data.length === 0){
              mlayer.warning('请选择数据');
              throw '请选择数据';
          }
          var ids = buildIds(data);
          return ids.split(',');
	  }

	  /**
	   * funs : 操作
	   * options : 配置
	   */
	  this.funs = function(funs){
		//操作
		  var defaultFuns = {
		      //批量删除
		      batchdel: function(){
				var idsSize = self.getCheckedIds().length;
		        var ids = self.getCheckedIds().join('');
		        layer.confirm('确定要删除这' + idsSize +'条数据吗？', function(index) {
		        	mlayer.load();
		        	Net.post({
	                	url : 'api/'+ self.globalOptions.module +'/deleteByIds',
	                	data : {ids : ids},
	                	success : function(res){
	                		if(res.code == 0) {
	                			self.refresh();
	                			layer.close(index); //执行关闭
								mlayer.success('删除成功');
	                		}else {
	                			mlayer.error('删除失败，' + res.msg)
	                		}
                            mlayer.closeLoad();
	                	}
	              })
		        });
		      },
		      //添加
		      add: function(othis){
				  if(self.callbacks.add.isFull) {
                      self.callbacks.add.area = [getScreenW() + 'px', getScreenH() + 'px'];
				  }
		    	  mlayer.open(self.callbacks.add.title, 'add', function(index){
					  form.render();
                      var $form = $('[lay-filter=FORM-ADD]');
                      //渲染时间
                      var formdateSize = $form.find('[formdate]').size();
                      if(formdateSize != 0) {
                          $form.find('[formdate]').each(function(i, o){
                              var type = $(this).attr('formdate-type');
                              if(type == null) {
                              	type = 'date';
							  }
                              var range = $(this).attr('formdate-range');
                              if(range == 'true') {
                                  range = true;
							  }else {
                                  range = false;
							  }
                              if(type == 'date') {
                                  renderDate({elem : $(o)[0], range:range});
                              }else if(type == 'datetime') {
                                  renderDatetime({elem : $(o)[0], range:range});
                              }else if(type == 'datetimeminute'){
                                  renderDatetimeminute({elem : $(o)[0], range:range});
                              }
                          });
                      }

		              if(self.callbacks.add.openCallback != null) {
		            	  self.callbacks.add.openCallback();
		              }

		              //监听提交
		              form.on('submit(ADD-SUBMIT)', function(data){
		                var field = data.field; //获取提交的字段
                        if(self.callbacks.add.submitCallback != null) {
                              self.callbacks.add.submitCallback(field);
                        }

                        if(self.callbacks.add.debug) {
                        	console.info('debug add form data');
                        	console.info(field);
                        	return false;
						}

						var url = 'api/'+ module +'/add';
                        var data = field;
                        if(self.callbacks.add.success == null) {
                            self.callbacks.add.success = function(){
                            	mlayer.success('添加成功');
                                self.refresh();
                                closeFormPage();
							}
						}
						ajaxFormSubmit(url, data, self.callbacks.add.success, self.callbacks.add.fail, self.callbacks.add.confirmMsg);
		              });
		    	  }, self.callbacks.add.data, self.callbacks.add.area);
		      } 
		  }
		var curFuns = $.extend(defaultFuns, funs);
		//表格上面的按钮点击
	    $('.function-list .layui-btn.layuiadmin-btn-list').on('click', function(){
	      var type = $(this).data('type');
	      curFuns[type] ? curFuns[type].call(this) : '';
	    });
	  }
	  
	  //自动刷新
	  this.autoRefresh = function(isAutoRefresh){
		  if(isAutoRefresh == null) isAutoRefresh = true;
		  var $form = $('[lay-filter='+ LaySearchBtn +']').parents('.search-form').first();
		  $form.find('input:radio').each(function(i, o){
			   var layFilter = $(this).attr('lay-filter');
			   layui.form.on('radio('+ layFilter +')', function(data){
				   console.info('input:radio');
				   searchFormSubmit();
			   }); 
			})
			  
			$form.find('select').each(function(){
				var filter = $(this).attr('lay-filter');
				layui.form.on('select('+ filter +')', function(data){
					console.info('select');
					searchFormSubmit();
				}); 
			});
		  
		  function searchFormSubmit(){
			  $('[lay-filter='+ LaySearchBtn +']').click();
		  }
	  }
	  
	  //监听工具条
	  this.tableTool = function(options){
		  options = $.extend({
				callback : function(obj){
					var data = obj.data; //获得当前行数据
					var layEvent = obj.event; //获得 lay-event 对应的值
					var tr = obj.tr; //获得当前行 tr 的DOM对象
				}
				/*
				confirm : [
					{
						event : 'disable', //lay-event的值
						method : 'updateStatus',   //后台接口的名字
						ask : '确定要禁用此账号吗？'  //提示用户的文字
					},
					{
						event : 'able',
						method : 'updateStatus',
						ask : '确定要启用此账号吗？'
					},
					{
						event : 'resetPwd',
						method : 'resetPwd',
						ask : '确定要重置此账号的密码吗？',
						notice : function(){  //自定义通知，默认用layer.msg()
							layer.alert('密码重置成功，初始密码为：123456');
						}
					}
				]*/
			}, options);
		  
		  
		  //监听工具条
		  table.on('tool('+ LayTable +')', function(obj){
			  var row = obj.data; //获得当前行数据
			  var layEvent = obj.event; //获得 lay-event 对应的值
			  var tr = obj.tr; //获得当前行 tr 的DOM对象
		    //删除
		    if(layEvent === 'del'){
		      layer.confirm('您确定要删除吗？', function(index){
                  mlayer.load();
		    	  Net.post({
	                	url : 'api/'+ globalOptions.module +'/deleteById',
	                	data : {id : row.id},
	                	success : function(res){
	                		if(res.code == 0) {
	                			obj.del();
	                			layer.close(index); //执行关闭
								mlayer.success('删除成功');
	                		}else {
                                mlayer.error('删除失败，' + res.msg);
	                		}
                            mlayer.closeLoad();
	                	}
	              })
		      });
		    //编辑
		    } else if(layEvent === 'edit'){
                if(self.callbacks.edit.isFull) {
                    self.callbacks.edit.area = [getScreenW() + 'px', getScreenH() + 'px'];
                }
		    	mlayer.open(self.callbacks.edit.title, 'update', function(index){

                    if(self.callbacks.edit.openCallback != null) {
                        self.callbacks.edit.openCallback(row);
                    }

		    		formval('FORM-UPDATE', row);
                    self.callbacks.edit.fillDataCallback(row);

		            //监听提交
		            form.on('submit(UPDATE-SUBMIT)', function(data){
		                var field = data.field; //获取提交的字段
						field.id = row.id;

                        if(self.callbacks.edit.submitCallback != null) {
                            self.callbacks.edit.submitCallback(field);
                        }

                        if(self.callbacks.add.debug) {
                            console.info('debug edit form data');
                            console.info(field);
                            return false;
                        }
                        if(self.callbacks.edit.success == null) {
                            self.callbacks.edit.success = function(){
                                mlayer.success('修改成功');
                                self.refresh();
                                closeFormPage();
                            }
                        }
			      		ajaxFormSubmit('api/'+ globalOptions.module +'/update', field, self.callbacks.edit.success, self.callbacks.edit.fail, self.callbacks.edit.confirmMsg);
		            });
		    	}, row, self.callbacks.edit.area);
		    //详情
		    }else if(layEvent == 'detail') {
		    	mlayer.open(self.callbacks.detail.title, wrapPagePrefix('detail'), function(){
		    		 form.render(null, 'layuiadmin-app-form-list');
		    	}, row, self.callbacks.detail.area);
		    }else {
                //询问框之类的功能
                var confirms = options.confirms;
                if(confirms != null) {
                    var cur = null;
                    $(confirms).each(function(i, o){
                        if(layEvent == o.event) {
                            cur = o;
                            return true;
                        }
                    });
                    if(cur != null) {
                        //得到点击事件对应的按钮
                        var btn = $(tr).find('[lay-event=' + cur.event + ']');
                        var data = btn.data();
                        data.id = row.id;
                        mlayer.confirm(cur.ask, function(i){
                            mlayer.load();
                            Lock.lock();
                            Net.post({
                                url : 'api/'+ globalOptions.module +'/' + cur.method,
                                data : data,
                                success : function(data){
                                    layer.close(i);
                                    mlayer.closeLoad();
                                    if(cur.notice == null) {
                                        if(data.code == 0) {
                                            mlayer.success('操作成功');
                                            self.refresh();
                                        }else {
                                            mlayer.error('操作失败，' + data.msg);
                                        }
                                    }else {
                                        cur.notice(data);
                                    }
                                    Lock.unlock();
                                },
                                error : function(){
                                    layer.close(loadIndex);
                                    layer.error('网络错误，请重试');
                                    Lock.unlock();
                                }
                            });
                        });
                    }
                }

                if(options.callback != null) {
		    		options.callback(obj);
		    	}
		    }
		  });
	  }
	  
	//表格中的开关
	this.tableSwitch = function(options){
			options = $.extend({
				field : 'isAdmin'  //开关对应的字段名
			}, options);
			
			layui.form.on('switch(' + options.field + ')', function(data){
				var id = $(data.elem).data('id');
				var is = 0;
				if(data.elem.checked) {
					is = 1;
				}
				var params = {id : id};
				params[options.field] = is;
				
				var field = options.field;
				field = field.charAt(0).toUpperCase() + field.substring(1, field.length)  //首字母变大写
				defenderCallbacks.intercept = function(){
					if(is == 1) {
				    	data.elem.checked = false;
				    }else {
				        data.elem.checked = true;
				    }
					layui.form.render();
				}
				ajaxFormSubmit('api/'+ globalOptions.module +'/update' + field, params, function (result) {
	      			mlayer.success('操作成功');
	            }, function(res){
	            	mlayer.error("操作失败，" + res.msg);
				    if(is == 1) {
				    	data.elem.checked = false;
				    }else {
				        data.elem.checked = true;
				    }
				    layui.form.render();
	            }, null, false);
			})
		}
		
		//单元格编辑
		this.cellEdit = function(options){
			options = $.extend({
				//验证函数
				verify : {
					
				},
				callback : function(obj){
					var value = obj.value; //得到修改后的值
					var field = obj.field; //当前编辑的字段名
					var data = obj.data;  //整行数据
				}
			}, options);
			var verify = options.verify; //开发者传过来的验证对象
			//单元格编辑
			table.on('edit('+ LayTable +')', function(obj){
			  var value = obj.value; //得到修改后的值
			  var field = obj.field; //当前编辑的字段名
			  var data = obj.data;  //整行数据
			  
			  //编辑sortCount单元格
			  if(field == 'sortCount') {
			    if(isNaN(value)) {
			      mlayer.error('必须输入数字');
			      self.refresh();
			      return false;
			    }else {
			      Net.post({
			      	url : 'api/'+ globalOptions.module +'/updateSortCount',
			      	data : { id : data.id, value : value},
			      	success : function(res){
			      		if(res.code == 0) {
                            mlayer.success('操作成功');
			      			self.refresh();
			      		}else {
                            mlayer.error('操作失败，' + res.msg);
                            self.refresh();
                            return false;
						}
			      	}
			      });
			    }
			  }else {
				  var fun = verify[field];
				  var errMsg = null;
				  if(fun != null) {
                      errMsg = fun(value, data);
				  }
				  if(hasText(errMsg)) {
                      mlayer.error(errMsg);
					  self.refresh();
				      return false;
				  }else {
					  var newFieId = upperFirstCase(field);
					  Net.post({
					      url : 'api/'+ globalOptions.module +'/update' + newFieId,
					      data : { id : data.id, value : value},
					      success : function(res){
					      	if(res.code == 0) {
                                mlayer.success('操作成功');
					      	}else {
                                mlayer.error('操作失败，' + res.msg);
                                self.refresh();
							}
	  			      	  }
					  });
				  }
			  }
			});
		}
	  
	  //统一渲染
	  this.render = function(options){
	  	  var self = this;

		  this.callbacks = options.callbacks;
		  if(this.callbacks == null) {
			  this.callbacks = {};
		  }
		  if(this.callbacks.add == null) {
			  this.callbacks.add = {};
		  }
		  
		  if(this.callbacks.add.title == null) {
			  this.callbacks.add.title = '添加';
		  }
		  
		  if(this.callbacks.edit == null) {
			  this.callbacks.edit = {};
		  }
		  
		  if(this.callbacks.edit.title == null) {
			  this.callbacks.edit.title = '编辑';
		  }
		  
		  if(this.callbacks.detail == null) {
			  this.callbacks.detail = {};
		  }
		  
		  if(this.callbacks.detail.title == null) {
			  this.callbacks.detail.title = '详情';
		  }
		  
		  if(this.callbacks.tableTool == null) {
			  this.callbacks.tableTool = {};
		  }

		  if(options.callbacks.search == null) {
			  this.callbacks.search = {};
		  }
		  
		  this.table(options);
		  this.tableTool(this.callbacks.tableTool);
		  this.search(options.callbacks.search.callback);
		  this.funs(options.funs);

		  var tableSwitches = options.tableSwitches;
		  if(tableSwitches != null && tableSwitches.length > 0) {
              tableSwitches.forEach(function(o){
                  self.tableSwitch(o);
			  })
		  }
		  this.cellEdit(options.cellEdit);
		  this.autoRefresh(options.autoRefresh);

	  }
	  
  }
  
  //自定义的弹层，比layer更通用
  mlayer = {
  /**
   * 弹出一个页面
   * title : 标题
   * url : 要渲染的页面的路径
   * done : 页面渲染完毕回调
   * params : 传给要弹出页面的参数，在子页面内可以直接拿到
   * area : layer的宽高
   * cancel : 关闭回调
   * otherOptions : 其他参数
   */
	open : function(title, url, done, params, area, cancel, otherOptions){
		  if(url.indexOf('/') == -1) {
			  url = wrapPagePrefix(url);
		  }
		  console.info('open url->' + url);
		  var options = {
			  title : title,
			  area : area == null ? ['550px', '80%'] : area,
			  shadeClose : false,
			  success : function(layero, index){
				  view(this.id).render(url, params).done(function(){
					  if(done != null) {
						  done(index);
						  console.info('index index->' + index);
                          window.FormPageIndex = index;
					  }
				  });
			  },
			  cancel : cancel
			  
		  }
		  
		  return admin.popup($.extend(options, otherOptions));
	},
	//弹出html
	openHtml : function(title, html, area){
		return layer.open({
		  title : title,
		  type: 1,
		  skin: 'layui-layer-rim', //加上边框
		  area: ['1000px', '500px'], //宽高
		  content: '<div class="mlayer-content">' + html + '</div>'
		});
	},
	//加载框
    load : function(){
    	mlayer.loadIndex = layer.load(2);
    },
    //关闭加载框
    closeLoad : function(){
    	layer.close(mlayer.loadIndex);
    },
    //询问框
    confirm : function(text, success, cancel){
    	layer.confirm(text, {title : '询问'}, success, cancel);
    },
    //错误提示
    error : function(msg){
		this.notice.error(msg);
    },
    //成功提示
    success : function(msg){
        this.notice.success(msg);
    },
	//警告提示
	warning : function(msg){
		this.notice.warning(msg);
	},
	native : {
        error : function(msg){
            layer.msg(msg, {
                icon: 5
                ,shift: 6
            });
        },
        //成功提示
        success : function(msg){
            layer.msg(msg, {
                icon: 6
            });
        },
	},
    //确定提示
    alert : {
    	error : function(msg, yes, success){
    		layer.alert(msg, {
  		      icon: 5,
  		      closeBtn : 0,
  		      yes : function(index){
  		    	  if(yes != null) {
  		    		  yes();
  		    	  }
  		    	  layer.close(index);
  		      },
  		      success : success
    		});
    	},
    	success : function(msg, yes, success){
    		layer.alert(msg, {
  		      icon: 6,
  		      closeBtn : 0,
  		      yes : function(index){
		    	  if(yes != null) {
		    		  yes();
		    	  }
		    	  layer.close(index);
		      },
		      success : success
    		});
    	}
    },
    //通知提示
    notice : {
    	success : function(msg){
    		notice.success(msg);
    	},
    	error : function(msg){
    		notice.error(msg);
    	},
    	warning : function(msg){
    		notice.warning(msg);
    	},
    	info : function(msg){
    		notice.info(msg);
    	}
    }
  }
  
  /**
   * 
   * 直接调用静态方法就可以使用
   * lock() : 获取锁，true表示获取成功可以继续，false表示获取失败，一旦调用此方法锁就会锁定，
   * 		   必须调用unlock()才能解锁，才能继续获取锁
   * unlock()：解锁，解锁之后lock()方法可以继续获取锁
   * 
   * 若是需要多个锁，可以new Lock();
   * 
   */
  Lock = function () {
  	this.locked = false;
  	this.lock = function() {
  		if(!this.locked) {
  			console.info('锁成功');
  			this.locked = true;
  		}else {
  			throw '已经上锁';
  		}
  	};
  	this.unlock = function() {
  		console.info('解锁');
  		this.locked = false;
  	};
  };
  Lock.locked = false;
  Lock.lock = function() {
  	if(!Lock.locked) {
  		console.info('锁成功');
  		Lock.locked = true;
  	}else {
  		throw '已经上锁';
  	}
  };
  Lock.unlock = function() {
  	console.info('解锁');
  	Lock.locked = false;
  };

  //null和空字符串返回false，其他返回true
  hasText = function(text){
  	var type = typeof text;
  	if(type === 'string' || text == undefined){
  		if(text != null && $.trim(text) != ''){
  			return true;
  		}else{
  			return false;
  		}
  	}
  	return true;
  }
  
  hasTextAndSet = function(key, obj, def){
	if(hasText(obj[key])) {
  		return obj[key];
  	}else {
  		return def ? def : '无';
  	}
  }

    /**
     * 初始化图片上传
     * elem : 元素的选择器，一般情况下只需要传入这个就行
     * @param options
     */
    initUploadImage = function(options) {
        options = $.extend({elem : '#image', accept : 'images', exts : 'jpg|jpeg|png', acceptMime:'image/jpeg, image/png', type : 'image', initMime : 'image', size : UploadImageMaxSize}, options);
        var result = initUploadFile(options);
        result.getUploadType = 'image';
        return result;
    }

    initUploadVideo = function(options) {
        options = $.extend({elem : '#video', accept : 'video', exts : 'mp4', acceptMime:'video/mp4', type : 'video', initMime : 'video', size : UploadVideoMaxSize}, options);
        var result = initUploadFile(options);
        result.getUploadType = 'video';
        return result;
    }
  
  /**
   * 初始化文件上传
   * elem : 元素的选择器，一般情况下只需要传入这个就行
   * @param options
   */
  initUploadFile = function (options) {
    console.debug('elem->' + options.elem);
    console.debug(options)
  	options = $.extend({
  	  elem: '#file', //绑定元素
  	  url: 'uploadFile', //上传接口
  	  accept : 'file',
  	  auto: false,
  	  data : {isPrivate : false},
  	  choose : function(obj){
  		obj.preview(function(index, file, result) {
			  var mime = file.type;
			  /**
			   * width
			   * height
			   * duration
			   */
			  var verify = options.verify;
			  if(verify != null) {
				  //图片验证尺寸验证
				  if(mime == 'image/jpeg' || mime == 'image/png') {
					  var reader = new FileReader();
					  reader.readAsDataURL(file);
					  reader.onload = function(theFile) {
						  var image = new Image();
						   image.src = theFile.target.result;
						   image.onload = function() {
							   if(verify.width != this.width || verify.height != this.height) {
								   mlayer.error('图片尺寸必须为' + verify.width + 'px*' + verify.height + 'px');
						       }else {
						    	   doUpload();
						       }
						   };
					  };
				  }
				  if(mime == 'video/mp4'){
					    var video = document.createElement('video');
					    video.preload = 'metadata';    //规定是否预加载视频。当页面加载后只载入元数据
					    video.onloadedmetadata = function() {  //当指定的音频/视频的元数据已加载时，会发生 loadedmetadata 事件。
					         window.URL.revokeObjectURL(video.src);//当图片加载完成后释放对象URL.
					         var duration = parseInt(video.duration)
					         console.log(video.videoWidth);
					         console.log(video.videoHeight);
					         if(verify.width != video.videoWidth || verify.height != video.videoHeight) {
					        	 mlayer.error('视频尺寸必须为' + verify.width + 'px*' + verify.height + 'px');
					         }else if(verify.duration != null && Number(verify.duration) != duration){
					        	 mlayer.error('视频时长必须为' + verify.duration + 's');
					         }else {
					    	   doUpload();
					         }
					    }
					   video.src = URL.createObjectURL(file);//方法会根据传入的参数创建一个指向该参数对象的URL.
				  }
			  }else {
					  doUpload();
			  }
			  function doUpload(){
				  mlayer.load();
				  obj.upload(index, file);
			  }
	      });
  	  },
  	  before : function(obj){
  		//mlayer.load();
  	  },
  	  done: function(res){
  		preview(res);
  		$(options.elem).data('key', res.key);
	    $(options.elem).data('size', res.size);
	    $(options.elem).data('mime', res.mime);
        $(options.elem).removeError();
  		mlayer.closeLoad();
  	  },
  	  error: function(){
  		mlayer.closeLoad();
  	  }
  	}, options);

    layui.upload.render(options);
    
    var initKey = options.initKey;
    if(!hasText(initKey)) {
    	initKey = $(options.elem).data('key');
	}
	var initMime = options.initMime;
	if(!hasText(initMime)) {
        initMime = $(options.elem).data('mime');
	}
    console.debug('initKey->' + initKey);
    console.debug('initMime->' + initMime);
    if(hasText(initKey)) {
    	preview({
    		key : initKey,
    		mime : initMime
    	});
    }
    
    function preview(res){
		var key = res.key;
		var mime = res.mime;
		var $elem = $(options.elem);
		var $previewEle = $elem.parent().parent().next();
		if($previewEle[0] != null && $previewEle.find('.form-upload-preview').size() != 0){
            $previewEle.remove();
		}
		var previewHtml = [];
        previewHtml.push('<div class="layui-form-item">');
        previewHtml.push('<label class="layui-form-label"></label>');
        previewHtml.push('<div class="layui-input-inline">');
		previewHtml.push('<div class="form-upload-preview">');
		//图片
		if(mime.indexOf('image') != -1) {
			previewHtml.push('<div class="layui-form-mid layui-word-aux form-upload-preview-container"><img class="form-upload-preview-image" lay-tips="点击看大图" src="'+ imageUrl(key) +'"/>');
		}else { //其他
			previewHtml.push('<div class="layui-form-mid layui-word-aux form-upload-preview-container"><icon class="layui-icon layui-icon-file a form-upload-preview-file" lay-tips="点击下载" data-url="'+ fileUrl(key) +'"/>');
		}
		previewHtml.push('<icon class="layui-icon layui-icon-close-fill form-upload-preview-delete"></icon>');
		previewHtml.push('</div>');
		previewHtml.push('</div>');
        previewHtml.push('</div>');
		$elem.parent().parent().after(previewHtml.join(''));
	}
  	
  	return {
  		getKey : function(){
  			return $(options.elem).data('key');
  		},
  		getWidth : function(){
  			return $(options.elem).data('width');
  		},
  		getHeight : function(){
  			return $(options.elem).data('height');
  		},
  		getSize : function(){
  			return $(options.elem).data('size');
  		},
  		getMime : function(){
  			return $(options.elem).data('mime');
  		}
  	};
  }


    initMultipleUploadImage = function(options) {
        options = $.extend({elem : '#image', accept : 'images', exts : 'jpg|jpeg|png', acceptMime:'image/jpeg, image/png', type : 'image', size : UploadImageMaxSize}, options);
        var result = initMultipleUploadFile(options);
        result.getUploadType = 'image';
        return result;
    }

    initMultipleUploadVideo = function(options) {
        options = $.extend({elem : '#video', accept : 'video', exts : 'mp4', acceptMime:'video/mp4', type : 'video', size : UploadVideoMaxSize}, options);
        var result = initMultipleUploadFile(options);
        result.getUploadType = 'video';
        return result;
    }
  //多文件上传
  initMultipleUploadFile = function(options){
      options = $.extend({
          elem: '#file', //绑定元素
          url: 'uploadFile', //上传接口
          accept : 'file',
          auto: true,
          data : {isPrivate : false},
          multiple: true,
		  done : function(res){
              addNewImage(res.key);
              $(options.elem).data('keys', getMultipleKeys(options.elem));
              mlayer.closeLoad();
		  }
	  }, options);

      layui.upload.render(options);

      //预览初始化
      var initKeys = options.initKeys;
      if(initKeys != null) {
          initKeys.forEach(function(o, i){
              addNewImage(o);
		  })
          $(options.elem).data('keys', getMultipleKeys(options.elem));
	  }

      function addNewImage(key){
          var html = [];
          html.push('<div class="form-upload-multiple-preview-item" data-key="'+ key +'">');
          html.push('<div class="form-upload-preview-image-wrapper">');
          var url = imageUrl(key);
          html.push('<img class="form-upload-preview-image" src="'+ url +'"/>');
          html.push('</div>');
          html.push('<div class="form-upload-multiple-preview-item-close"><a class="layui-icon layui-icon-close-fill"></a></div>');
          html.push('</div>');
          $(options.elem).parent().find('.form-upload-multiple-preview').append(html.join(''));
      }

      function getMultipleKeys(btnSelector, split){
          if(split == null) {
              split = ',';
          }
          var keys = [];
          console.info('getMultipleKeys')
		  console.info('btnSelector->' + btnSelector)
          $(btnSelector).parent().find('.form-upload-multiple-preview-item').each(function(i, o){
              keys.push($(this).data('key'));
          })
          return keys.join(split);
      }

      $(document).off('click', '.form-upload-multiple-preview-item-close a').on('click', '.form-upload-multiple-preview-item-close a', function(){
          var $this = $(this);
          var $btn = $(this).parent().parent().parent().parent().find('.layui-btn');
          mlayer.confirm('确定要删除此图片吗？', function(index){
              $this.parents('.form-upload-multiple-preview-item').first().remove();
              layer.close(index);
              $btn.data('keys', getMultipleKeys('[name=' + $btn.attr("name") + ']'));
          })
      })

	  return {
      	getKeys : function(){
      		return getMultipleKeys(options.elem);
		}
	  }
  }
  
  //上传的预览
  bindUploadPreview = function(){
	  $(document).on('click', '.form-upload-preview-image', function(e){
		  var src = e.currentTarget.currentSrc;
		  previewImage(src);
	  });
	  
	  $(document).on('click', '.form-upload-preview-file', function(e){
		  var url = $(e.currentTarget).data('url');
		  location.href = url;
	  });

	  //先取消绑定再绑定，防止重复绑定的问题
	  $(document).off('click', '.form-upload-preview-delete').on('click', '.form-upload-preview-delete', function(e){
		  var $this = $(this);
		  mlayer.confirm('确定删除吗？', function(i){
			  var $btn = $this.parent().parent().parent().parent().prev().find('button');
			  $btn.data('key', null);
			  $btn.data('width', null);
			  $btn.data('height', null);
			  $btn.data('size', null);
			  $btn.data('mime', null);
			  $this.parent().parent().parent().parent().remove();
			  layer.close(i);
		  });
	  });
  }
  
  //预览图片
  previewImage = function(url){
	  layer.photos({
		  photos : {
			  data : [{src : url}]
		  }
	  });
  }
  
  //渲染表单页面
  renderFormPage = function(verifies, callback) {
	  form.render();
	  renderRequiredMark();
      $('.readonly').prop('readonly', true);
	  form.verify(getFormVerify(verifies, callback));
  }
  
  //禁用整个表单,传入表单的选择器，一定要自己起一个特殊的名字，不然会把别的表单也给禁用了
  disableForm = function(selector){
	  $(selector).addClass('disabled');
	  $(selector).find('input').prop('disabled', true);
	  $(selector).find('select').prop('disabled', true);
	  $(selector).find('textarea').prop('disabled', true);
	  $(selector).find('button').prop('disabled', true);
	  form.render();
  }
  
  //表单设置初始值
  formval = function(layFilter, vals){
  	    var vals = $.extend({}, vals);
	    Object.keys(vals).forEach(function(key){
	    	if(vals[key] != null) {
	    		if(typeof vals[key] == 'number') {
	    			vals[key] = vals[key] + '';
	    		}
	    	}
	    })
		form.val(layFilter, vals);
		var $form = $('[lay-filter='+ layFilter +']');
		
		//渲染时间
		var formdateSize = $form.find('[formdate]').size();
		if(formdateSize != 0) {
			$form.find('[formdate]').each(function(i, o){
				var type = $(this).attr('formdate-type');
                var min = $(this).attr('formdate-min');
                var max = $(this).attr('formdate-max');
				var name = $(this).attr('name');
				var val = vals[name] ? parseInt(vals[name]) : '';
				var obj = {
                	elem : $form.find('[name='+ name +']')[0],
					value : new Date(val),
					range :false,
					min : min,
					max : max
				}
				if(type == 'date') {
					renderDate(obj);
				}else if(type == 'datetime') {
                    renderDatetime(obj);
				}else if(type == 'datetimeminute'){
                    renderDatetimeminute(obj);
				}
			});
		}
		
		//渲染上传
		var formuploadSize = $form.find('[formupload]').size();
		if(formuploadSize != 0) {
			$form.find('[formupload]').each(function(i, o){
                var type = $(this).attr('formupload-type');
                var name = $(this).attr('name');
                var val = vals[name] ? vals[name] : '';
				var mp = $(this).parent().find('.form-upload-multiple-preview');
				if(mp.size() == 0) {
					console.info("upload val->" + val);
                    if(val != null) {
                        $(this).data('key', val);
                        $(this).data('mime', type);
                    }
				}
			});
		}
		form.render();
	};
  
  //对表单中的上传控件进行非空验证
  customFormVerifyRequired = function(callback){
	    var innerCustomFormVerifyRequired = function(val, elem){
	    	val = $.trim(val);
	    	var name = $(elem).attr('name');
	    	var requiredType = $(elem).attr('required-type');
	    	var requiredMsg = $(elem).attr('required-msg');
	    	requiredMsg = requiredMsg == null ? '必填项不能为空' : requiredMsg;

	    	if(requiredType == 'upload') {
	    		var key = $(elem).data('key');
                var keys = $(elem).data('keys');
	    		if(!hasText(key) && !hasText(keys)) {
                    $(elem).addError();
	    			return requiredMsg;
	    		}else {
                    $(elem).removeError();
	    		}
	    	}else if(requiredType == 'checkbox'){
	    		var checkboxName = $(elem).find('input').first().attr('name');
	    		var checkedSize = $('input:checkbox[name='+ checkboxName +']:checked').size();
	    		if(checkedSize == 0) {
                    $(elem).addError();
	    			return requiredMsg;
	    		}else {
                    $(elem).removeError();
	    		}
	    	}else if(requiredType == 'radio'){
	    		var radioName = $(elem).find('input').first().attr('name');
	    		var checkedSize = $('input:radio[name='+ radioName +']:checked').size();
	    		if(checkedSize == 0) {
                    $(elem).addError();
	    			return requiredMsg;
	    		}else {
                    $(elem).removeError();
	    		}
	    	}else {
	    		if(callback != null) {
	    			var errMsg = callback(name, requiredType, requiredMsg);
	    			if(hasText(errMsg)) {
	    				return errMsg;
	    			}
	    		}else {
		    		if(val == '') {
		    			return requiredMsg;
		    		}
	    		}
	    	}
	    };
	    return innerCustomFormVerifyRequired;
  }

    /**
	 * 判断字符串是否是个json字符串
     * @param str
     * @returns {boolean}
     */
	isJson = function (str) {
		if (typeof str == 'string') {
			try {
				var obj=JSON.parse(str);
				if(typeof obj == 'object' && obj ){
					return true;
				}else{
					return false;
				}

			} catch(e) {
				console.log('error：'+str+'!!!'+e);
				return false;
			}
		}
		console.log('It is not a string!')
	}

  getFormVerify = function(verifies, callback){
	  verifies = $.extend({
		  required : customFormVerifyRequired(callback),
		  zzs : [/^[1-9]\d*$/, '必须输入正整数'],
		  pass : [/^[\w_]{6,16}$/, '密码必须在6-16位，并且只能包含数字、字母和下划线_'],
		  money : [/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/, '金额格式不正确，必须大于0且最多只能有两位小数'],
		  json : function(val){
			  if(hasText($.trim(val)) && !isJson(val)) {
		  		  return '必须输入json字符串';
			  }
		  }
	  }, verifies);
	  return verifies;
  }
  
  
  //表单提交
  formSubmit = function(layFilter, url, handleFields, success, confirmMsg){
	  form.on('submit('+ layFilter +')', function(data){
		    if(hasText(confirmMsg)) {
		    	mlayer.confirm(confirmMsg, function(){
		    		submit(data);
		    	});
		    }else {
		    	submit(data);
		    }
			return false;
	  });
	  
	  function submit(data){
		Lock.lock();
		mlayer.load();
		var postData = data.field;
		if(handleFields != null) {
			handleFields(postData);
		}
		Net.post({
			url : url,
			data : postData,
			success : function(res){
				mlayer.closeLoad();
				
				if(res.code == -2) {
					DefenderTeam[res.data]();
					return false;
				}
				
				if(success != null) {
					success(res);
					Lock.unlock();
				}else {
					mlayer.success('操作成功');
					Lock.unlock();
				}
			}
		})
	  }
  }
  
  //异步表单提交
  ajaxFormSubmit = function(url, data, success, fail, confirmMsg, loading){
	  if(hasText(confirmMsg)) {
    	mlayer.confirm(confirmMsg, function(index){
    		submit(index);
    	});
	  }else {
	    submit();
	  }
	function submit(index){
		Lock.lock();
		if(loading) {
			mlayer.load();
		}
		Net.post({
			url : url,
			data : data,
			success : function(res){
				Lock.unlock();
				if(res.code == -2) {
					defenderCallbacks.intercept();
					DefenderTeam[res.data]();
					return false;
				}
				
				if(res.code == 0) {
					if(success != null) {
						success(res);
					}else {
						mlayer.success('操作成功');
					}
				}else {
					if(fail != null) {
						fail(res);
					}else {
						mlayer.error('操作失败，' + res.msg);
					}
				}
                layer.close(index);  //关闭询问框
                mlayer.closeLoad(); //关闭加载框
			}
		})
	}
  }
  
  //jquery对象增加和移除错误框
  $.fn.extend({
	   addError : function(msg){
	   	   var type = this.attr('required-type');
           var tagName = this.prop('tagName');
           if(tagName == 'BUTTON') {
               this.addClass('custom-form-danger');
		   }else if(tagName == 'DIV') {
               if(type == 'checkbox' || type == 'radio') {
                   this.parent().addClass('custom-form-danger');
               }else {
                   this.addClass('layui-form-danger');
                   this.focus();
               }
		   }
		   if(msg) {
			   mlayer.native.error(msg);
		   }
	   },
	   removeError : function(){
		   var tagName = this.prop('tagName');
           if(tagName == 'BUTTON') {
               this.removeClass('custom-form-danger');
		   }else {
               this.parent().removeClass('custom-form-danger');
		   }

	   }
  });
  //稍后执行
  later = function(fun, delay){
	  if(delay == null) {
		  delay = 3000;
	  }
	  setTimeout(function(){
		  fun();
	  }, delay);
  }
  
  getParam = function(name){
	  return layui.router().search[name];
  }
  
  pageback = function(){
	  history.back();
  }
  
  pageerror = function(){
	  location.hash = '/common/error';
  }
  
  page404 = function(){
	  location.hash = '/common/404';
  }
  
  //把日期转成毫秒
  str2ms = function(dateStr){
  	var oldTime = (new Date(dateStr)).getTime();
  	return oldTime;
  }
  //把毫秒转成日期
  ms2str = function (ms, type){
	if(type == null) {
		type == 'datetimeminute';
	}
  	Date.prototype.toLocaleString = function() {
  		var month = this.getMonth() < 9 ? '0'+(this.getMonth()+1) : this.getMonth()+1;
  		var date = this.getDate() < 10 ? '0'+this.getDate() : this.getDate();
  		var hours = this.getHours() < 10 ? '0'+this.getHours() : this.getHours();
  		var minutes = this.getMinutes() < 10 ? '0'+this.getMinutes() : this.getMinutes();
  		var seconds = this.getSeconds() < 10 ? '0'+this.getSeconds() : this.getSeconds();
          if(type == 'date') {
          	  return this.getFullYear() + "/" + month + "/" + date;
          }else if(type == 'datetime'){
        	  return this.getFullYear() + "/" + month + "/" + date + " " + hours + ":" + minutes + ":" + seconds;
          }else if(type == 'datetimeminute') {
          	  return this.getFullYear() + "/" + month + "/" + date + " " + hours + ":" + minutes;
          }
      };
  	var date = new Date(ms);
  	return date.toLocaleString();
  }
  
//对$.ajax的一些封装
  Net = {
      get : function(option){
          //用户直接传url
          if((typeof option) == 'string') {
              var result = null;
              $.ajax({
                  url : option,
                  type : 'get',
                  dataType : 'json',
                  async : false, //同步
                  success : function(data) {
                      result = data;
                  },
                  error : function() {
                      console.error('请求出错');
                      mlayer.closeLoad();
                      mlayer.error('网络错误');
                      Lock.unlock();
                  }
              });
              return result;
          //用户传ajax的配置参数
          }else {
              option = $.extend({
                  type : 'get',
                  dataType : 'json',
                  error : function(){
                	mlayer.closeLoad();
                	mlayer.error('网络错误');
                  	Lock.unlock();
                  }
              }, option);
              $.ajax(option);
          }
      },
      post : function(option, data){
          //用户直接传url
          if((typeof option) == 'string') {
              var result = null;
              $.ajax({
                  url : option,
                  type : 'post',
                  dataType : 'json',
                  async : false, //同步
                  data : data,
                  success : function(data) {
                      result = data;
                  },
                  error : function() {
                	  console.error('请求出错');
                      mlayer.closeLoad();
                      mlayer.error('网络错误');
                      Lock.unlock();
                  }
              });
              return result;
          //用户传ajax的配置参数
          }else {
              option = $.extend({
                  type : 'post',
                  dataType : 'json',
                  error : function(){
                	  console.error('请求出错');
                      mlayer.closeLoad();
                      mlayer.error('网络错误');
                      Lock.unlock();
                  }
              }, option);
              $.ajax(option);
          }
      }
  };
  
  /**
   * 步骤组件
   * @param selector 外层div的选择器
   * @param stayOn 停留在第几步，从0开始
   * @param steps 每一步的文字，数组类型
   * @param ovveride append()和html()的区别
   */
  initSteps = function(selector ,stayOn, steps, override) {
		var html = ['<div class="layui-slider">'];
		var oneStep = 100/(steps.length - 1);
		for(var i = 0; i < steps.length; i ++) {
			html.push('<div class="layui-slider-step step-o" style="left: '+ (oneStep * i) +'%; ">');
			if(i <= stayOn) {
				html.push('<div class="step-text">'+ steps[i] +'</div>');
			}else {
				html.push('<div class="step-text step-disabled">'+ steps[i] +'</div>');
			}
			html.push('</div>');
			if(i == stayOn) {
				html.push('<div class="layui-slider-bar step-bg" style="width: '+ (oneStep * i) +'%;"></div>');
				html.push('<div class="layui-slider-wrap step-O-wrap" style="left: '+ (oneStep * i) +'%;">');
				html.push('<div class="layui-slider-wrap-btn step-O"></div>');
				html.push('</div>');
			}
		}
		html.push('</div>');
		if(override == null) {
			override = false;
		}
		if(override) {
			$(selector).html(html.join(''));
		}else {
			$(selector).append(html.join(''));
		}
	}
  
  buildIds = function(list, split){
  	    if(split == null) {
  	    	split = ',';
		}
		var ids = '';
		if(list != null && list.length > 0) {
			$(list).each(function(i, o){
				ids += o.id;
				ids += split;
			});
			ids = ids.substring(0, ids.length - 1);
		}
		return ids;
	}
  
  /**
   * 获取复选框的值
   * @param selecter
   * @param split 分隔符号
   * @returns
   */
  getCheckboxValues = function(selecter, split) {
  	var values = [];
      if($(selecter + ':checked').size() > 0) {
      	$($(selecter + ':checked')).each(function(i, o){
      		values.push(o.value);
      	});
      }
      if(split == null) {
          split = ',';
	  }
      return values.join(split);
  }
  /**
   * 获取复选框的显示值，以逗号分隔
   * @param selecter
   * @returns
   */
  getCheckboxTexts = function(selecter, split) {
  	var values = [];
      if($(selecter + ':checked').size() > 0) {
      	$($(selecter + ':checked')).each(function(i, o){
      		values.push($(this).attr('title'));
      	});
      }
      if(split == null) {
          split = ',';
	  }
      return values.join(split);
  }
  wrapTag = function(val) {
  	return '<span class="tag">'+ val +'</span>';
  }
  
  wrapMoney = function(fen){
	  return '<ran class="money">￥'+ moneyYuan(fen) +'</ran>'
  }
  
  wrapMoneyYuan = function(yuan){
	  return '<ran class="money">￥'+ yuan +'</ran>'
  }
  
  wrapDetailFile = function(key){
	  return '<a href="'+ fileUrl(key) +'" target="_blank"><icon class="layui-icon layui-icon-file a" lay-tips="点击下载"></icon><a>';
  }
  
  wrapDetailText = function(text){
	  if(hasText(text)) return text;
	  return wrapInfoGrey('无');
  }
  
  //变为元
  moneyYuan = function(money) {
  	return parseFloat(money / 100).toFixed(2);
  }
  //int转为百分比小数
  int2per = function(int){
      return parseFloat(int / 100).toFixed(2);
  }

  //乘法运算
  accMul = function(arg1,arg2){
  	var m=0,s1=arg1.toString(),
  	s2=arg2.toString();  
  	try{
  	m+=s1.split(".")[1].length}catch(e){}  
  	try{
  	m+=s2.split(".")[1].length}catch(e){}  
  	return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
  }

  //变为分
  moneyFen = function(money){
  	return accMul(money, 100);
  }

  /**
   * 切割以逗号分隔的字符串，并且以标签列表的形式展现
   * @param val
   * @returns
   */
  splitAndToTagList = function(val) {
  	if(hasText(val)) {
  		var arr = val.split(',');
  		var html = [];
  		$(arr).each(function(i, o){
  			html.push(wrapTag(o));
  		});
  		return html.join('');
  	}else {
  		return '';
  	}
  }

  wrapInfoBlue = function(val) {
  	return '<span class="info-blue">'+ val +'</span>';
  }

  wrapInfoGreen = function(val) {
  	return '<span class="info-green">'+ val +'</span>';
  }

  wrapInfoYellow = function(val) {
  	return '<span class="info-yellow">'+ val +'</span>';
  }

  wrapInfoRed = function(val) {
  	return '<span class="info-red">'+ val +'</span>';
  }

  wrapInfoGrey = function(val) {
  	return '<span class="info-grey">'+ val +'</span>';
  }
  
  wrapInfoSilvery = function(val) {
	  	return '<span class="info-silvery">'+ val +'</span>';
	  }
  
  wrapInfoGoldren = function(val) {
	  	return '<span class="info-goldren">'+ val +'</span>';
	  }

  wrapInfo = function(val) {
  	return '<span class="info">'+ val +'</span>';
  }
  
  //包裹大师分
  wrapMasterScore = function(score){
	  var html = [];
	  if(score.b > 0) {
		  html.push(wrapInfoBlue('蓝分：' + score.b));
	  }
	  if(score.r > 0) {
		  html.push(wrapInfoRed('红分：' + score.r));
	  }
	  if(score.s > 0) {
		  html.push(wrapInfoSilvery('银分：' + score.s));
	  }
	  if(score.g > 0) {
		  html.push(wrapInfoGoldren('金分：' + score.g));
	  }
	  return '<div class="table-infos">' + html.join('') + '</div>';
  }
  
  //根据回车分隔
  strSplitByEnter = function(str){
  	return str.split("\n");
  }
  
  //判断是否是正整数
  isZZS = function(num){
  	return /^[1-9]\d*$/.test(num);
  }

  //大写第一个字母 
  upperFirstCase = function(str){
  	return str.substring(0,1).toUpperCase() + str.substring(1);
  }
  
  //渲染必填项的红色*
  renderRequiredMark = function(selector) {
	if(selector == null) {
		selector = '.ADD [lay-verify], .UPDATE [lay-verify], FORM [lay-verify]';
	}
  	$(selector).each(function(i, o){
  		var layVerify = $(this).attr('lay-verify');
  		if(layVerify.indexOf('required') != -1) {
  			addRequired(o);
  		}
  	});
  	
  	function addRequired(o) {
  		var ignoreMark = $(o).attr('ignore-mark');
  		if(ignoreMark == null) {
  			var $label = $(o).parent().parent().find('.layui-form-label');
  			if(!$label.find('.required')[0]) {
  				$label.html('<span class="required">*</span>' + $label.html());
  			}
  		}
  	}
  }
  
  //验证码相关
  initVerifyCodeImg = function(selector){
	  var lock = new Lock();
	  if(selector == null) {
		  selector = '.verify-code-img';
	  }
	  if($(selector)[0]) {
	  		//验证码
	  		$(selector).click(function(){
	  			lock.lock();
	  			changeVerifyCode(selector);
	  		});
	  		
	  		$(selector)[0].onload = function() {
	  			lock.unlock();
	  		}
	  		$(selector)[0].onerror = function() {
	  			lock.unlock();
	  		}
	  	}
  }

  //更新验证码
  changeVerifyCode = function(selector) {
  	var src = 'getVerifyCode?ms=' + new Date().getTime();
  	$(selector).attr('src', src);
  }
  
  /**
   * 点击复制
   * 调用此方法会查找所有的.copy的元素，在点击后会将元素上面的data-text的值放到
   * 剪切板，若传了getTextFun的方法，则会把此方法的返回值放到剪切板，getTextFun
   * 方法的参数是点击的按钮的jquery对象
   */
  copy = function(btnSelector, getTextFun) {
  	var idSelector = 'LAY-TEXT-COPY';
  	if(!$(idSelector)[0]) {
  		$('body').append('<input type="text" id="'+ idSelector +'"/>');
  	}
  	var content = $('#' + idSelector);
  	$(btnSelector).click(function(){
  		var text = $(this).data('text');
  		if(!hasText(text)) {
  			text = getTextFun($(this));
  		}
  		content.val(text);
  		content.select();
  		document.execCommand('Copy');
  		mlayer.success('已复制');
  	})
  }
  
  /**
   * val是否在arr里面
   * @param val
   * @param arr
   */
  inArr = function(val, arr){
	  if(arr == null) return false;
	  return $.inArray(val, arr) != -1;
  }
  
  var DefendLayerIndex = 0;
  //验证机制
  var DefenderTeam = {
		SOFT_SMS : function(){
			DefendLayerIndex = mlayer.open('手机验证', 'defend/softSMSDefend', function(){
				
			}, {}, ['513px', '272px'], function(){
				defenderCallbacks.cancel();
				Lock.unlock();
			})
		},
		LOGIN : function(){
			DefendLayerIndex = mlayer.open('手机验证', 'defend/loginDefend', function(){
				
			}, {}, ['513px', '272px'], function(){
				defenderCallbacks.cancel();
				Lock.unlock();
			}, {
				skin : 'layui-layer-admin-login',
			})
		}
  };
  
  //验证回调
  defenderCallbacks = {
	  cancel : function(){
		  
	  },
      pass : function(){
    	  layer.close(DefendLayerIndex);
      },
      intercept : function(){
    	  
      }
  };
  
  defenderCallback = function(callbacks){
	  if(callbacks.cancel != null) {
		  defenderCallbacks.cancel = callbacks.cancel;
	  }
	  if(callbacks.pass != null) {
		  defenderCallbacks.pass = function(){
			  layer.close(DefendLayerIndex);
			  callbacks.pass();
		  }
	  }
  }

    getScreenW = function(){
        return $(window).width();
    }

    getScreenH = function(){
        return $(window).height();
    }
  
  /***********************************业务相关公告方法-start************************************/
  renderGameLevelList = function(code, selector){
	    if(selector == null) {
	    	selector = '#gameLevelList';
	    }
		var gameLevels;
		if(hasText(code)) {
			gameLevels = Net.get('api/game/getGameLevelList?projectCode=' + code);
		}else {
			gameLevels = Net.get('api/game/getMyGameLevelList');
		}
		var html = [];
		gameLevels.forEach(function(o, i){
			html.push('<li class="'+ (i ? '' : 'layui-this') +'" data-type="'+ o.type +'" data-code="'+ o.code +'">'+ o.name +'</li>');
		});
		$(selector).html(html.join(''));
		return gameLevels;
  }
  
  wrapGameA = function(gameid, isAlert) {
	  if(isAlert == null) {
		  return '<a class="a" lay-href="/game/list/changList/gameid='+ gameid +'">'+ gameid +'</a>';
	  }else {
		  return '<a class="a open-game-detail" data-id="'+ gameid +'">'+ gameid +'</a>';
	  }
  }
  
  wrapGameAs = function(gameids, isAlert) {
	  var html = [];
	  gameids.forEach(function(o){
		  html.push(wrapGameA(o, isAlert));
	  })
	  return html.join('<br>');
  }
  
  //若wrapGame的是弹层的那种，则需要调用此方法绑定事件
  renderWrapGameAlert = function(){
	  $(document).on('click', '.open-game-detail', function(){
		  var id = $(this).data('id');
		  mlayer.open("赛事详情", '/game/apply/detail', function(){
	    		 form.render(null, 'layuiadmin-app-form-list');
	      }, {id:id}, ['550px', '500px']);
	  })
  }

    handleRole = function(role){
        if(role == 'Consumer') {
            return wrapInfoBlue('普通消费者');
        }else if(role == 'Svip') {
            return wrapInfoGreen('超级会员');
        }else if(role == 'ServiceProvider') {
            return wrapInfoBlue('服务商');
        }else if(role == 'CityPartner') {
            return wrapInfoGreen('城市合伙人');
        }else if(role == 'Company') {
            return '公司';
        }else {
            return '-';
        }
    }
  /*************************************业务相关公告方法-end*************************************/


  
  /******************************公共执行*******************************************/
  bindUploadPreview();
  renderBreadcrumb();
  //加载完毕后触发
  Pace.on('done', function(){
  });
  
  //退出
  admin.events.logout = function(){
    //执行退出接口
    admin.req({
      url: './json/user/logout.js'
      ,type: 'get'
      ,data: {}
      ,done: function(res){ //这里要说明一下：done 是只有 response 的 code 正常才会执行。而 succese 则是只要 http 为 200 就会执行
        
        //清空本地记录的 token，并跳转到登入页
        admin.exit();
      }
    });
  };
  //对外暴露的接口
  exports('common', {});
});
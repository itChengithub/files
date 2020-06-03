<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<aside class="main-sidebar">
	<!-- sidebar: style can be found in sidebar.less -->
	<section class="sidebar">
		<!-- Sidebar user panel -->
		<div class="user-panel">
				<div class="pull-left image">
					<img src="${pageContext.request.contextPath}/img/user2-160x160.jpg"
						 class="img-circle" alt="User Image">
				</div>
				<div class="pull-left info">
					<p><security:authentication property="principal.username"></security:authentication></p>
					<a href="#"><i class="fa fa-circle text-success"></i> 在线</a>
				</div>
		</div>

		<!-- sidebar menu: : style can be found in sidebar.less -->
		<ul class="sidebar-menu">
			<li class="header">菜单</li>
			<li id="admin-index"><a
				href="${pageContext.request.contextPath}"><i
					class="fa fa-dashboard"></i> <span>首页</span></a></li>
			<security:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_USER')">
			<li class="treeview"><a href="#"> <i class="fa fa-cogs"></i>
					<span>系统管理</span> <span class="pull-right-container"> <i
						class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">
					<security:authorize access="hasRole('ROLE_ADMIN')">
					<li id="system-setting3"><a
						href="${pageContext.request.contextPath}/user/findAll?page=1&size=5"> <i
							class="fa fa-circle-o"></i> 用户管理
					</a></li>
					</security:authorize>
					<li id="system-setting4"><a
						href="${pageContext.request.contextPath}/role/findAll?page=1&size=5"> <i
							class="fa fa-circle-o"></i> 角色管理
					</a></li>
					<li id="system-setting5"><a
						href="${pageContext.request.contextPath}/permission/findAll?page=1&size=5">
							<i class="fa fa-circle-o"></i> 资源权限管理
					</a></li>
					<li id="system-setting6"><a
						href="${pageContext.request.contextPath}/sysLog/findAll?page=1&size=5"> <i
							class="fa fa-circle-o"></i> 访问日志
					</a></li>
				</ul></li>
			</security:authorize>
			<li class="treeview"><a href="#"> <i class="fa fa-cube"></i>
					<span>基础数据</span> <span class="pull-right-container"> <i
						class="fa fa-angle-left pull-right"></i>
				</span>
			</a>
				<ul class="treeview-menu">

					<li id="system-setting7"><a
						href="${pageContext.request.contextPath}/product/findAllPage?page=1&size=5">
							<i class="fa fa-circle-o"></i> 产品管理
					</a></li>
					<li id="system-setting8"><a
						href="${pageContext.request.contextPath}/order/findAllPage?page=1&size=5"> <i
							class="fa fa-circle-o"></i> 订单管理
					</a></li>

				</ul></li>

		</ul>
	</section>
	<!-- /.sidebar -->
</aside>
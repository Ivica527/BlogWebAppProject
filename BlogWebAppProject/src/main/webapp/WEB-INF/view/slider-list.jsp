<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE html>
<!--
This is a starter template page. Use this page to start your new project from
scratch. This page gets rid of all links and provides the needed markup only.
-->
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="x-ua-compatible" content="ie=edge">

  <title>Cubes school - Blog project</title>

  <!-- Font Awesome Icons -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/plugins/fontawesome-free/css/all.min.css">
  <script src="https://kit.fontawesome.com/b2f601a4f0.js" crossorigin="anonymous"></script>
  <!-- Theme style -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/dist/css/adminlte.min.css">
  <!-- Google Font: Source Sans Pro -->
  <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,400i,700" rel="stylesheet">  
 
</head>
<body class="hold-transition sidebar-mini">
<div class="wrapper">

  <!-- Navbar -->
  <nav class="main-header navbar navbar-expand navbar-white navbar-light">
    <!-- Left navbar links -->
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" data-widget="pushmenu" href="#" role="button"><i class="fas fa-bars"></i></a>
      </li>
    </ul>    
    <!-- Right navbar links -->
    <ul class="navbar-nav ml-auto">
    <li class="nav-item dropdown">
    
    <c:if test="${commentCount==0}">    
    	<a class="nav-link" data-toggle="dropdown" href="#" aria-expanded="false">
          <i class="far fa-comments"></i>          
        </a>    
    </c:if>
    
    <c:if test="${commentCount>0}">
   	 <a class="nav-link" data-toggle="dropdown" href="#" aria-expanded="false">
          <i class="far fa-comments"></i>
          <span class="badge badge-danger navbar-badge">${commentCount}</span>
        </a>
        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right" style="left: inherit; right: 0px;">        
          <div class="dropdown-divider"></div>
          <a href="comment-list" class="dropdown-item dropdown-footer">See All Comments</a>
        </div>
    </c:if>
      </li>
    <li class="nav-item dropdown">        
        <c:if test="${messageCount==0}">        
        <a class="nav-link" data-toggle="dropdown" href="#">
          <i class="fa-regular fa-envelope"></i>
        </a>
        </c:if>
        <c:if test="${messageCount>0}">
        <a class="nav-link" data-toggle="dropdown" href="#">
          <i class="fa-regular fa-envelope"></i>
          <span class="badge badge-warning navbar-badge">${messageCount}</span>
        </a>
        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
          <div class="dropdown-divider"></div>
          <a href="message-list" class="dropdown-item dropdown-footer">See All Messages</a>
        </div>
        </c:if>
      </li>
      <!-- Messages Dropdown Menu -->
      <li class="nav-item dropdown">
        <a class="nav-link" data-toggle="dropdown" href="#">
          <i class="far fa-user"></i>
        </a>
        <div class="dropdown-menu dropdown-menu-lg dropdown-menu-right">
          <a href="user-edit-profile" class="dropdown-item">
            <!-- Message Start -->
            <div class="media align-items-center">
              <img src="${user1.image}" alt="User Avatar" style="max-width: 10rem;"  class="img-brand-50 mr-3 img-circle">
              <div class="media-body">
                <h3 class="dropdown-item-title">
                  <sec:authentication property="principal.username"/>
                  <p>Change information</p>
                </h3>
              </div>
            </div>
            <!-- Message End -->
          </a>
          <div class="dropdown-divider"></div>
          <a href="user-change-password" class="dropdown-item">
            <i class="fas fa-user"></i> Change password
          </a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item">              
              <form:form action="${pageContext.request.contextPath}/logout">              
              <input type="submit" value="Sign out">              
              </form:form>
          </a>
        </div>
      </li>
    </ul>
  </nav>
  <!-- /.navbar -->
  
  
   

  <!-- Main Sidebar Container -->
  <aside class="main-sidebar sidebar-dark-primary elevation-4">
    <!-- Product Logo -->
    <a href="index3.html" class="brand-link">
      <img src="${pageContext.request.contextPath}/dist/img/AdminLTELogo.png" alt="Cubes School Logo" class="brand-image img-circle elevation-3"
           style="opacity: .8">
      <span class="brand-text font-weight-light">Cubes School </span><br></br>      
      <span style="margin-left: 45px;color: orange;" >- Blog project -</span>
    </a>

		<div class="user-panel mt-3 pb-3 mb-3 d-flex">	
    	 <div class="image">
          <img src="${user1.image}" class="img-circle elevation-2" alt="User Image">
        </div>
        <div class="info">
          <a href="#" class="d-block">${user1.name} ${user1.surname}</a>
        </div>
     	 </div>


    <!-- Sidebar -->
    <div class="sidebar">
      <!-- Sidebar Menu -->
       <nav class="mt-2">
        <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
          <!-- Add icons to the links using the .nav-icon class
               with font-awesome or any other icon font library -->
          <li class="nav-item has-treeview">
            <a href="#" class="nav-link">
              <i class="nav-icon far fa-plus-square"></i>
              <p>
                Blogs
                <i class="right fas fa-angle-left"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="blog-list" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Blog list</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="blog-form" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Add Blog</p>
                </a>
              </li>
            </ul>
          </li>
          <li class="nav-item has-treeview">
            <a href="#" class="nav-link">
              <i class="nav-icon far fa-plus-square"></i>
              <p>
                Categories
                <i class="right fas fa-angle-left"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="category-list" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Categories list</p>
                </a>
              </li>
              <sec:authorize access="hasRole('admin')">
              <li class="nav-item">
                <a href="category-form" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Add Category</p>
                </a>
              </li>
              </sec:authorize> 
            </ul>
          </li>
          <li class="nav-item has-treeview">
            <a href="#" class="nav-link">
              <i class="nav-icon far fa-plus-square"></i>
              <p>
                Tags
                <i class="right fas fa-angle-left"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="tag-list" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Tags list</p>
                </a>
              </li>
              <sec:authorize access="hasRole('admin')">  
              <li class="nav-item">
                <a href="tag-form" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Add Tag</p>
                </a>
              </li>
              </sec:authorize>
            </ul>
          </li>
          <sec:authorize access="hasRole('admin')">        
          <li class="nav-item has-treeview">
            <a href="#" class="nav-link">
              <i class="nav-icon far fa-plus-square"></i>
              <p>
                Sliders
                <i class="right fas fa-angle-left"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="slider-list" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Slider list</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="slider-form" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Add Slider</p>
                </a>
              </li>
            </ul>
          </li>
           </sec:authorize>
          <li class="nav-item has-treeview">
            <a href="#" class="nav-link">
              <i class="nav-icon far fa-plus-square"></i>
              <p>
                Users
                <i class="right fas fa-angle-left"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="user-list" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>User list</p>
                </a>
              </li>
              <sec:authorize access="hasRole('admin')"> 
              <li class="nav-item">
                <a href="user-form" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Add User</p>
                </a>
              </li>
              </sec:authorize> 
            </ul>
          </li>
          <sec:authorize access="hasRole('admin')">              
          <li class="nav-item has-treeview">
            <a href="#" class="nav-link">
              <i class="nav-icon far fa-plus-square"></i>
              <p>
                Other
                <i class="right fas fa-angle-left"></i>
              </p>
            </a>
            <ul class="nav nav-treeview">
              <li class="nav-item">
                <a href="message-list" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Message list</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="comment-list" class="nav-link">
                  <i class="far fa-circle nav-icon"></i>
                  <p>Comment list</p>
                </a>
              </li>              
            </ul>
          </li> 
          </sec:authorize>
          <li class="nav-item has-treeview">
            <a href="/BlogWebAppProject" class="nav-link">
              <i class="nav-icon far fa-plus-square"></i>
              <p >
                GO TO FRONT PAGE                
              </p>
            </a>            
          </li>
                 
        </ul>
      </nav>
      <!-- /.sidebar-menu -->
    </div>
    <!-- /.sidebar -->
  </aside>

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1>Sliders</h1>
          </div>
        </div>
      </div><!-- /.container-fluid -->
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="container-fluid">
        <div class="row">
          <div class="col-md-12">
            <div class="card">
              <div class="card-header">
                <h3 class="card-title">All Sliders</h3>
                <div class="card-tools">
                  <a href="slider-form" class="btn btn-info btn-sm">
                    <i class="fas fa-plus-square"></i>
                    Add new Slider
                  </a>
                </div><br></br>
                
                 <div class="card-tools">
                   <button type="button" style="background-color: orange;" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#reset">
                      <i class="fas fa-minus-circle"></i>Reset Slider list                            
                   </button>
                	<div class="modal fade show" id="reset" >
			        <div class="modal-dialog">
			          <div class="modal-content bg-info">
			            <div class="modal-header">
			              <h4 class="modal-title">Reset Slider list</h4>
			              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			                <span aria-hidden="true">&times;</span></button>
			            </div>
			            <div class="modal-body">
			              <p>Are you sure you want to reset slider list on main page?</p>
			               <p>If you reset the list of sliders, no slider will be displayed on the main page</p>
			               <p>You need to select slider again on the button "IS VISIBLE ON MAIN PAGE"</p>
			            </div>
			            <div class="modal-footer justify-content-between">
			              <button type="button" class="btn btn-outline-light" data-dismiss="modal">Cancel</button>
			              <a href="slider-list-reset" style="background-color: orange;"  class="btn btn-danger btn-outline-light">Reset</a>	              
			            </div>
			          </div>
			          <!-- /.modal-content -->
			        </div>
			        <!-- /.modal-dialog -->
			      </div>	
                  
                </div>
                
                
              </div>
              <!-- /.card-header -->
              <div class="card-body">
              <div  class="card-body">
                <p>Setovanjem dugmeta "IS VISIBLE ON MAIN PAGE" na on/off  automatski  slajderu dodeljujete "NUMBER IN ORDER" i obrnuto (brisete),</p>
              <p>to je redosled kojim ce biti prikazivani na slajderu, a sam slajder je ogranicen na broj unetih slajdera</p>
              <p>Redosled kojim ce biti prikazan je sledeci (dat je id i naziv slajdera):</p>
              <c:if test="${sliderList0.size()==0}">
              <span style="color: red; background-color: yellow;">NEMA OZNACENIH SLIDERA ZA PRIKAZ NA GLAVNOJ STRANI</span>
              </c:if>              
              <c:if test="${sliderList0.size()>0}">
              <c:forEach var="slider" items="${sliderList0}">              
              <p><span style="color: gray; background-color: lime;">id(${slider.id}) : slider "${slider.title}" </span></p>              
              </c:forEach>
              </c:if>              
                </div>
                <table class="table table-bordered">
                  <thead>                  
                    <tr>
                      <th class="text-center">ID</th>
                      <th class="text-center">TITLE</th>
                      <th class="text-center">IMAGE</th>
                      <th class="text-center">TITLE BUTTON</th>
                      <th class="text-center">URL BUTTON</th>
                      <th class="text-center">IS VISIBLE ON MAIN PAGE</th>
                      <th class="text-center">NUMBER IN ORDER</th>                      
                      <th class="text-center">ACTIONS</th>
                    </tr>
                  </thead>
                  <tbody>
                  
                  <c:forEach var="slider" items="${sliderList}">	
					<tr>
					<td>${slider.id}</td>
					<td style="text-transform: uppercase;">${slider.title}</td>
					<td class="text-center">
                        <img src="${slider.image}" style="max-width: 80px;">
                    </td>
					<td>${slider.titleButton}</td>
					<td>${slider.urlButton}</td>
					<td class="text-center">
					
					 <c:if test="${slider.isVisible}">
                        	<a href="slider-isVisible?id=${slider.id}" class="btn btn-info">
                            	<i class="fa-solid fa-eye"></i>
                          	</a>                        
                        </c:if> 
                        
                        <c:if test="${!slider.isVisible}">
                        	<a href="slider-isVisible?id=${slider.id}" class="btn btn-info">
                            	<i class="fa-solid fa-eye-slash" style="color:#ffd31d;"></i>
                          	</a>                        
                        </c:if> 
					</td>
					
					
					<c:if test="${slider.numberInOrder>0}">
					<td class="text-center" style="font-size: 1.5rem;">
					${slider.numberInOrder}</td>
					</c:if>
					<c:if test="${slider.numberInOrder==0}">
					<td>
					</td>
					</c:if>					
					<td class="text-center">
                        <div class="btn-group">
                        <a class="btn btn-info btn-sm" href="slider-form-update?id=${slider.id}">
                              <i class="fa-solid fa-pen-nib"></i>
                              </i><div>Edit</div>
                              
                          </a>
                          <button type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#delete-modal-${slider.id}">
                            <i class="fa-solid fa-trash-can"></i>
                            <div>Delete</div>
                          </button>
                          
                        </div>
                      </td>	        
                      				
                    </tr>
				    <div class="modal fade show" id="delete-modal-${slider.id}" >
			        <div class="modal-dialog">
			          <div class="modal-content bg-info">
			            <div class="modal-header">
			              <h4 class="modal-title">Delete Slider</h4>
			              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
			                <span aria-hidden="true">&times;</span></button>
			            </div>
			            <div class="modal-body">
			              <p>Are you sure you want to delete slider?</p>
			            </div>
			            <div class="modal-footer justify-content-between">
			              <button type="button" class="btn btn-outline-light" data-dismiss="modal">Cancel</button>
			              <a href="slider-delete?id=${slider.id}" class="btn btn-danger btn-outline-light">Delete</a>	              
			            </div>
			          </div>
			          <!-- /.modal-content -->
			        </div>
			        <!-- /.modal-dialog -->
			      </div>		
					</c:forEach>
                  </tbody>
                </table>
                
              </div>              
              <!-- /.card-body -->
              
              <div class="card-footer clearfix">
                
              </div>
              
            </div>
            <!-- /.card -->
          </div>
          <!-- /.col -->
        </div>
        <!-- /.row -->
      </div><!-- /.container-fluid -->
    </section>
    <!-- /.content -->
    <!-- /.modal -->
      </div>
  <!-- /.content-wrapper -->
  <!-- Main Footer -->
  <footer class="main-footer">
    <!-- To the right -->
    <div class="float-right d-none d-sm-inline">
      Java
    </div>
    <!-- Default to the left -->
    <strong>Copyright &copy; 2019 <a href="https://cubes.edu.rs">Cubes School</a>.</strong> All rights reserved.
  </footer>
</div>
<!-- ./wrapper -->

<!-- REQUIRED SCRIPTS -->

<!-- jQuery -->
<script src="${pageContext.request.contextPath}/plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap 4 -->
<script src="${pageContext.request.contextPath}/plugins/bootstrap/js/bootstrap.bundle.min.js"></script>
<!-- AdminLTE App -->
<script src="${pageContext.request.contextPath}/dist/js/adminlte.min.js"></script>
<script src="https://kit.fontawesome.com/b2f601a4f0.js" crossorigin="anonymous"></script>

</body>
</html>
<template>
  <div class="app-container">
    <!-- 侧边栏 -->
    <aside class="sidebar">
      <div class="logo">
        <img src="./assets/logo.png" alt="Logo">
        <span>海洋环境灾害智能预测系统</span>
      </div>
      
      <nav class="main-nav">
        <div class="nav-section">
          <div class="nav-header collapsible" @click="toggleSection('overview')">
            <span>海域概览</span>
            <i class="dropdown-icon" :class="{'open': openSections.overview}">▼</i>
          </div>
          <ul v-show="openSections.overview">
            <li><a href="#" @click.prevent="showMap" :class="{ 'active': activeView === 'map' }"><i class="icon">B</i> 全景海域地图</a></li>
            <li><a href="#" @click.prevent="showCoastline" :class="{ 'active': activeView === 'coastline' }"><i class="icon">G</i> 海岸线示意图</a></li>
          </ul>
        </div>
        
        <div class="nav-section">
          <div class="nav-header collapsible" @click="toggleSection('waterQuality')">
            <span>水质要素监测数据</span>
            <i class="dropdown-icon" :class="{'open': openSections.waterQuality}">▼</i>
          </div>
          <ul v-show="openSections.waterQuality">
            <li><a href="#" @click.prevent="showDongshanPanel" :class="{ 'active': activeView === 'dongshan' }"><i class="icon">Y</i> 大亚湾东山</a></li>
            <li><a href="#"><i class="icon">Y</i> 大亚湾东涌</a></li>
            <li><a href="#"><i class="icon">Y</i> 大亚湾坝光</a></li>
            <li><a href="#"><i class="icon">Y</i> 大亚湾长湾</a></li>
            <li><a href="#"><i class="icon">P</i> 大鹏湾下沙</a></li>
            <li><a href="#"><i class="icon">P</i> 大鹏湾南澳</a></li>
            <li><a href="#"><i class="icon">P</i> 大鹏湾大梅沙</a></li>
            <li><a href="#"><i class="icon">P</i> 大鹏湾湾口</a></li>
            <li><a href="#"><i class="icon">P</i> 大鹏湾沙头角</a></li>
            <li><a href="#"><i class="icon">S</i> 深圳湾蛇口</a></li>
            <li><a href="#"><i class="icon">Z</i> 珠江口内伶仃以南</a></li>
            <li><a href="#"><i class="icon">Z</i> 珠江口矾石</a></li>
          </ul>
        </div>
        
        <div class="nav-section">
          <div class="nav-header collapsible" @click="toggleSection('forms')">
            <span>赤潮预警</span>
            <i class="dropdown-icon" :class="{'open': openSections.forms}">▼</i>
          </div>
          <ul v-show="openSections.forms">
            <li><a href="#" @click.prevent="showRedTideTable('dapeng')" :class="{ 'active': activeView === 'redTideDapeng' }"><i class="icon">P</i> 大鹏湾海域</a></li>
            <li><a href="#"><i class="icon">Z</i> 珠江口海域</a></li>
            <li><a href="#"><i class="icon">Y</i> 大亚湾海域</a></li>
            <li><a href="#"><i class="icon">S</i> 深圳湾海域</a></li>
          </ul>
        </div>
        
        <!-- <div class="nav-section">
          <div class="nav-header collapsible" @click="toggleSection('pages')">
            <span>海洋生态稳定性监测</span>
            <i class="dropdown-icon" :class="{'open': openSections.pages}">▼</i>
          </div>
          <ul v-show="openSections.pages">
            <li><a href="#"><i class="icon">H</i> 历史数据</a></li>
            <li><a href="#"><i class="icon">A</i> 分析报告</a></li>
          </ul>
        </div> -->
      </nav>
    </aside>
    
    <!-- 主内容区 -->
    <main class="main-content">
      <!-- 顶部导航 -->
      <header class="top-nav">
        <div class="search-box">
          <input type="text" placeholder="Search...">
          <button class="search-btn">🔍</button>
        </div>
        <div class="top-nav-actions">
          <button class="stats-btn">📊 状态</button>
          <button class="notifications-btn">🔔 通知</button>
          <button class="settings-btn">⚙️</button>
        </div>
      </header>
      
      <!-- 内容区域 -->
      <div class="content-area">
        <h2 class="content-title" style="margin-left: 2.5%;">{{ contentTitle }}</h2>
        
        <!-- 地图容器 -->
        <div id="map-container" class="map-container" v-show="activeView === 'map'"></div>
        
        <!-- 海岸线图片容器 -->
        <div class="image-container" v-show="activeView === 'coastline'">
          <img :src="coastlineImage" alt="海岸线示意图" class="coastline-image">
        </div>
        
        <!-- 赤潮预警数据表格 - 大鹏湾 -->
        <div class="table-container" v-show="activeView === 'redTideDapeng'">
          <div class="table-controls">
            <div class="records-per-page">
              <select v-model="recordsPerPage">
                <option value="6">6</option>
                <option value="7">7</option>
                <option value="8">8</option>
              </select>
            </div>
            <div class="search-records">
              <input type="text" v-model="searchQuery" placeholder="搜索记录...">
            </div>
          </div>
          
          <table class="data-table">
            <thead>
              <tr>
                <th @click="sortBy('location')">监测位置 <i class="sort-icon" :class="getSortIconClass('location')"></i></th>
                <th @click="sortBy('date')">监测日期 <i class="sort-icon" :class="getSortIconClass('date')"></i></th>
                <th @click="sortBy('algaeType')">藻类种类 <i class="sort-icon" :class="getSortIconClass('algaeType')"></i></th>
                <th @click="sortBy('concentration')">最大细胞密度(个/L) <i class="sort-icon" :class="getSortIconClass('concentration')"></i></th>
                <th @click="sortBy('status')">最大成灾面积（平方千米） <i class="sort-icon" :class="getSortIconClass('status')"></i></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(record, index) in paginatedData" :key="index" :class="{ 'warning': record.status === '警告', 'danger': record.status === '危险' }">
                <td>{{ record.location }}</td>
                <td>{{ record.date }}</td>
                <td>{{ record.algaeType }}</td>
                <td>{{ record.concentration }}</td>
                <td>
                  <span class="status-dot" :class="getStatusClass(record.status)"></span>
                  {{ record.status }}
                </td>
              </tr>
            </tbody>
          </table>
          
          <div class="pagination">
            <div class="pagination-info">显示 {{ startRecord }} 至 {{ endRecord }} 条记录，共 {{ filteredData.length }} 条</div>
            <div class="pagination-controls">
              <button class="page-btn" @click="goToPage('prev')" :disabled="currentPage === 1">«</button>
              <button 
                v-for="page in displayedPages" 
                :key="page" 
                class="page-number" 
                :class="{ 'active': currentPage === page }"
                @click="goToPage(page)"
              >
                {{ page }}
              </button>
              <button class="page-btn" @click="goToPage('next')" :disabled="currentPage === totalPages">»</button>
            </div>
          </div>
          
          <!-- 只在大鹏湾海域界面显示警告通知 -->
          <div class="alert alert-warning" style="text-align: center;">
            <strong>预警 -</strong> 明天可能发生赤潮！
            <span class="close-btn" onclick="this.parentElement.style.display='none';" style="display: flex; align-items: center; justify-content: center;">&times;</span>
          </div>
        </div>
        
        <!-- 大亚湾东山面板 -->
        <div class="panel-container" v-show="activeView === 'dongshan'">
          <div class="panel-header">
            <h3>大亚湾东山水质监测数据</h3>
            <button class="close-btn" @click="closePanel">×</button>
          </div>
          <div class="panel-content">
            <div class="data-grid">
              <div class="data-item" v-for="(value, key) in dongshanData" :key="key">
                <div class="data-label">{{ getLabel(key) }}</div>
                <div class="data-value">{{ value }}</div>
              </div>
            </div>
            <div class="charts-container">
              <div class="chart-wrapper" v-for="(chart, index) in 8" :key="index">
                <canvas :ref="'chart' + index"></canvas>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 页脚 -->
      <footer class="footer">
      </footer>
    </main>
  </div>
</template>

<script>
import AMapLoader from '@amap/amap-jsapi-loader';
import { Chart } from 'chart.js/auto';
import coastlineImage from '@/assets/R.jpg'
import '@/style/main.css'
import '@/style/table.css'

export default {
  name: 'App',
  data() {
    return {
      map: null,
      // 深圳市中心坐标
      shenzhenCenter: [114.064839, 22.548857],
      // 大鹏湾坐标
      dapengBayCoords: [114.504364, 22.586678],
      markers: [],
      // 控制侧边栏各部分的展开/折叠状态
      openSections: {
        overview: true,
        waterQuality: false,
        forms: true,  // 默认展开赤潮预警菜单
        pages: false
      },
      // 当前激活的视图
      activeView: 'map',
      // 海岸线图片
      coastlineImage: coastlineImage,
      // 内容标题
      contentTitle: '深圳海域地图',
      // 显示警告通知
      showWarning: true,
      
      // 赤潮监测数据
      redTideData:[
        { location: '大鹏湾', date: '2021-01-20', algaeType: '棕囊藻', concentration: '2000000', status: '0.2' },
        { location: '大鹏湾', date: '2021-01-21', algaeType: '棕囊藻', concentration: '2300000', status: '0.3' },
        { location: '大鹏湾', date: '2021-01-23', algaeType: '棕囊藻', concentration: '26000000', status: '0.5' },
        { location: '大鹏湾', date: '2021-01-24', algaeType: '棕囊藻', concentration: '85000000', status: '0.5' },
        { location: '大鹏湾', date: '2021-01-25', algaeType: '棕囊藻', concentration: '44000000', status: '0.2' },
        { location: '大鹏湾', date: '2021-01-26', algaeType: '棕囊藻', concentration: '20000000', status: '0.5' },
        { location: '大鹏湾', date: '2021-01-27', algaeType: '棕囊藻', concentration: '80000000', status: '0.5' },
        { location: '大鹏湾', date: '2021-01-28', algaeType: '棕囊藻', concentration: '38000000', status: '0.2' },
        { location: '大鹏湾', date: '2021-01-28', algaeType: '红色赤潮藻', concentration: '47000000', status: '1.5' },
        { location: '大鹏湾', date: '2021-01-29', algaeType: '棕囊藻', concentration: '50000000', status: '0.5' },
        { location: '大鹏湾', date: '2021-01-30', algaeType: '棕囊藻', concentration: '10000', status: '0.02' },
        { location: '大鹏湾', date: '2021-01-31', algaeType: '棕囊藻', concentration: '290000', status: '1.0' },
        { location: '大鹏湾', date: '2021-02-01', algaeType: '棕囊藻', concentration: '54000000', status: '0.1' },
        { location: '大鹏湾', date: '2021-02-02', algaeType: '棕囊藻', concentration: '3300000', status: '0.05' },
        { location: '大鹏湾', date: '2021-03-02', algaeType: '红色赤潮藻', concentration: '9400000', status: '1.2' },
        { location: '大鹏湾', date: '2022-04-03', algaeType: '夜光藻', concentration: '900000', status: '1.0' },
        { location: '大鹏湾', date: '2022-04-07', algaeType: '夜光藻', concentration: '480000', status: '1.0' },
        { location: '大鹏湾', date: '2022-04-12', algaeType: '夜光藻', concentration: '40000', status: '0.0' }
      ],

      
      // 表格配置
      currentPage: 1,
      recordsPerPage: 6,
      searchQuery: '',
      sortKey: 'date',
      sortOrder: 'desc', // 'asc' 或 'desc'
      dongshanData: {
        chlorophyll: 0,
        waterTemperature: 0,
        dissolvedOxygen: 0,
        pH: 0,
        salinity: 0,
        pressure: 0,
        airTemperature: 0,
        relativeHumidity: 0
      },
      charts: [],
      chart: null
    }
  },
  computed: {
    // 过滤后的数据
    filteredData() {
      const query = this.searchQuery.toLowerCase();
      return this.redTideData.filter(record => {
        return Object.values(record).some(value => 
          String(value).toLowerCase().includes(query)
        );
      });
    },
    
    // 排序后的数据
    sortedData() {
      return [...this.filteredData].sort((a, b) => {
        let valueA = a[this.sortKey];
        let valueB = b[this.sortKey];
        
        // 处理数字类型的字段
        if (this.sortKey === 'concentration' || this.sortKey === 'status') {
          valueA = parseFloat(valueA);
          valueB = parseFloat(valueB);
        }
        
        // 排序逻辑
        if (valueA < valueB) return this.sortOrder === 'asc' ? -1 : 1;
        if (valueA > valueB) return this.sortOrder === 'asc' ? 1 : -1;
        return 0;
      });
    },
    
    // 分页后的数据
    paginatedData() {
      const start = (this.currentPage - 1) * parseInt(this.recordsPerPage);
      const end = start + parseInt(this.recordsPerPage);
      return this.sortedData.slice(start, end);
    },
    
    // 总页数
    totalPages() {
      return Math.ceil(this.filteredData.length / parseInt(this.recordsPerPage));
    },
    
    // 显示页码
    displayedPages() {
      const pages = [];
      let startPage = Math.max(1, this.currentPage - 2);
      let endPage = Math.min(this.totalPages, startPage + 4);
      
      // 确保始终显示5个页码（如果有的话）
      if (endPage - startPage < 8) {
        startPage = Math.max(1, endPage - 8);
      }
      
      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }
      
      return pages;
    },
    
    // 起始记录
    startRecord() {
      return this.filteredData.length ? (this.currentPage - 1) * parseInt(this.recordsPerPage) + 1 : 0;
    },
    
    // 结束记录
    endRecord() {
      return Math.min(this.startRecord + parseInt(this.recordsPerPage) - 1, this.filteredData.length);
    }
  },
  mounted() {
    this.initMap();
  },
  methods: {
    // 切换侧边栏部分的展开/折叠状态
    toggleSection(section) {
      // 阻止事件冒泡，确保点击子菜单项时不会触发折叠
      event.stopPropagation();
      // 只有点击标题区域才切换展开状态
      if (event.target.closest('.nav-header')) {
        this.openSections[section] = !this.openSections[section];
      }
    },
    
    // 显示地图
    showMap() {
      this.activeView = 'map';
      this.contentTitle = '深圳海域地图';
      // 如果地图还没初始化，则初始化
      if (!this.map) {
        this.$nextTick(() => {
          this.initMap();
        });
      }
    },
    
    // 显示海岸线示意图
    showCoastline() {
      this.activeView = 'coastline';
      this.contentTitle = '海岸线示意图';
      
      // 如果地图已初始化，缩小地图大小
      if (this.map) {
        this.$nextTick(() => {
          this.map.setZoom(8); // 设置较小的缩放级别
          this.map.setFitView(); // 调整视图以适应所有标记点
        });
      }
    },
    
    // 显示赤潮预警表格
    showRedTideTable(area) {
      if (area === 'dapeng') {
        this.activeView = 'redTideDapeng';
        this.contentTitle = '大鹏湾海域赤潮发生历史';
        this.currentPage = 1; // 重置页码
      }
    },
    
    // 关闭警告信息
    closeWarning() {
      this.showWarningAlert = false;
    },
    
    // 表格排序
    sortBy(key) {
      // 如果点击的是当前排序字段，则切换排序顺序
      this.sortOrder = (this.sortKey === key && this.sortOrder === 'asc') ? 'desc' : 'asc';
      this.sortKey = key;
    },
    
    // 获取排序图标样式
    getSortIconClass(key) {
      if (this.sortKey === key) {
        return this.sortOrder === 'asc' ? 'asc' : 'desc';
      }
      return '';
    },
    
    // 获取状态样式
    getStatusClass(status) {
      const value = parseFloat(status);
      if (value < 0.2) return 'normal';
      if (value >= 0.2 && value < 1.0) return 'warning';
      if (value >= 1.0) return 'danger';
      return '';
    },
    
    // 页面跳转
    goToPage(page) {
      if (page === 'prev') {
        this.currentPage = Math.max(1, this.currentPage - 1);
      } else if (page === 'next') {
        this.currentPage = Math.min(this.totalPages, this.currentPage + 1);
      } else {
        this.currentPage = page;
      }
    },
    
    initMap() {
      // 加载高德地图
      window._AMapSecurityConfig = {
        securityJsCode: '16ce5f72d817cb714e966e61894ec04f',
      }
      AMapLoader.load({
        key: '67b854404cdbc45a4eb0373842ad74ef', // 你的高德地图API密钥
        version: '2.0',
        plugins: [
          'AMap.ToolBar', 
          'AMap.Scale', 
          'AMap.OverView', 
          'AMap.MapType', 
          'AMap.Geolocation',
          'AMap.Weather'
        ]
      }).then((AMap) => {
        // 初始化地图
        this.map = new AMap.Map('map-container', {
          zoom: 12, // 适合查看城市级别的缩放
          center: this.dapengBayCoords, // 更改初始中心点为大鹏湾
          viewMode: '3D', // 3D视图
          mapStyle: 'amap://styles/light', // 浅蓝色风格适合海洋主题
          pitch: 25 // 稍微倾斜视角以获得3D效果
        });
        
        // 添加基本控件
        this.map.addControl(new AMap.ToolBar({
          position: 'RT' // 右上角
        }));
        this.map.addControl(new AMap.Scale());
        this.map.addControl(new AMap.MapType({
          defaultType: 1 // 默认显示卫星图
        }));
        
        // 添加定位控件
        const geolocation = new AMap.Geolocation({
          enableHighAccuracy: true, // 是否使用高精度定位，默认:true
          timeout: 10000, // 超过10秒后停止定位，默认：无穷大
          offset: [10, 20], // 定位按钮的停靠位置
          zoomToAccuracy: true, // 定位成功后调整地图视野范围使定位位置及精度范围视野内可见
          position: 'RB', // 定位按钮的位置，默认：'RB'（右下角）
          buttonPosition: 'RB', // 定位按钮的停靠位置
          buttonOffset: new AMap.Pixel(10, 20), // 定位按钮与设置的停靠位置的偏移量
          showButton: true // 显示定位按钮，默认：true
        });
        this.map.addControl(geolocation);
        
        // 监听定位成功事件
        AMap.event.addListener(geolocation, 'complete', (data) => {
          console.log('定位成功', data);
        });
        
        // 监听定位失败事件
        AMap.event.addListener(geolocation, 'error', (err) => {
          console.error('定位失败', err);
        });
        
        // 添加多个监测点标记
        this.addMonitoringPoints();
        
        // 添加大鹏湾永久性标记
        this.addDapengBayMarker();
      }).catch(e => {
        console.error('地图加载失败：', e);
      });
    },
    
    // 添加大鹏湾永久性标记
    addDapengBayMarker() {
      if (!this.map) return;
      
      const AMap = window.AMap;
      
      // 创建图标实例
      const icon = new AMap.Icon({
        // 使用高德地图提供的红色箭头标记
        image: 'https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png',
        size: new AMap.Size(36, 36),  // 图标大小
        imageSize: new AMap.Size(36, 36)
      });
      
      // 创建大鹏湾标记
      const dapengMarker = new AMap.Marker({
        position: this.dapengBayCoords,
        icon: icon,
        offset: new AMap.Pixel(-18, -36),  // 偏移量，使箭头尖端对准确切位置
        title: '大鹏湾',
        clickable: true,
        zIndex: 100,  // 确保在其他标记上方
        animation: 'AMAP_ANIMATION_BOUNCE'  // 添加弹跳动画效果，使标记更明显
      });
      
      // 创建信息窗口内容
      const content = `<div class="info-window">
                         <h3>大鹏湾</h3>
                         <p>经度: ${this.dapengBayCoords[0]}</p>
                         <p>纬度: ${this.dapengBayCoords[1]}</p>
                         <p>状态: 监测中</p>
                       </div>`;
      
      const infoWindow = new AMap.InfoWindow({
        content: content,
        offset: new AMap.Pixel(0, -36)
      });
      
      // 点击事件
      AMap.event.addListener(dapengMarker, 'click', () => {
        infoWindow.open(this.map, dapengMarker.getPosition());
      });
      
      // 将标记添加到地图
      this.map.add(dapengMarker);
      
      // 确保标记永不消失，即使地图缩放或移动
      AMap.event.addListener(this.map, 'zoomend', () => {
        // 确保标记仍在地图上
        if (!dapengMarker.getMap()) {
          this.map.add(dapengMarker);
        }
      });
      
      AMap.event.addListener(this.map, 'moveend', () => {
        // 确保标记仍在地图上
        if (!dapengMarker.getMap()) {
          this.map.add(dapengMarker);
        }
      });
      
      // 默认打开信息窗口，使标记更明显
      infoWindow.open(this.map, dapengMarker.getPosition());
    },
    
    // 添加监测点
    addMonitoringPoints() {
      if (!this.map) return;
      
      // 监测点位置（模拟数据）
      const monitoringPoints = [
        { name: '监测点1', position: [114.301493, 22.561504], status: 'normal' },
        { name: '监测点2', position: [114.231493, 22.531504], status: 'warning' },
        { name: '监测点3', position: [114.271493, 22.511504], status: 'normal' },
        { name: '监测点4', position: [114.321493, 22.541504], status: 'critical' }
      ];
      
      // 创建不同颜色的标记
      const AMap = window.AMap;
      
      monitoringPoints.forEach(point => {
        // 根据状态选择不同图标
        let iconUrl;
        switch (point.status) {
          case 'warning':
            iconUrl = 'https://webapi.amap.com/theme/v1.3/markers/n/mark_y.png';
            break;
          case 'critical':
            iconUrl = 'https://webapi.amap.com/theme/v1.3/markers/n/mark_r.png';
            break;
          default:
            iconUrl = 'https://webapi.amap.com/theme/v1.3/markers/n/mark_g.png';
        }
        
        // 创建标记
        const marker = new AMap.Marker({
          position: point.position,
          title: point.name,
          icon: new AMap.Icon({
            image: iconUrl,
            size: new AMap.Size(25, 34),
            imageSize: new AMap.Size(25, 34)
          }),
          clickable: true,
          animation: 'AMAP_ANIMATION_DROP' // 添加掉落动画效果
        });
        
        // 创建信息窗口
        const content = `<div class="info-window">
                           <h3>${point.name}</h3>
                           <p>状态: ${point.status === 'normal' ? '正常' : 
                                   point.status === 'warning' ? '警告' : '危险'}</p>
                           <p>数据最后更新: ${new Date().toLocaleString()}</p>
                         </div>`;
                         
        const infoWindow = new AMap.InfoWindow({
          content: content,
          offset: new AMap.Pixel(0, -30)
        });
        
        // 点击事件
        AMap.event.addListener(marker, 'click', () => {
          infoWindow.open(this.map, marker.getPosition());
        });
        
        this.map.add(marker);
        this.markers.push(marker);
      });
    },
    
    showDongshanPanel() {
      this.activeView = 'dongshan';
      this.contentTitle = '大亚湾东山水质监测';
      this.fetchDongshanData();
    },
    
    closePanel() {
      this.activeView = 'map';
      this.contentTitle = '深圳海域地图';
    },
    
    async fetchDongshanData() {
      try {
        const response = await fetch('http://localhost:8080/api/water-quality');
        const data = await response.json();
        console.log('Received data:', data);  // 添加调试日志
        if (data && data.length > 0) {
          // 更新最新数据
          const latestData = data[data.length - 1];
          console.log('Latest data:', latestData);  // 添加调试日志
          this.dongshanData = {
            chlorophyll: latestData.chlorophyll?.toFixed(2) || 0,
            waterTemperature: latestData.waterTemperature?.toFixed(2) || 0,
            dissolvedOxygen: latestData.dissolvedOxygen?.toFixed(2) || 0,
            pH: latestData.ph?.toFixed(2) || 0,
            salinity: latestData.salinity?.toFixed(2) || 0,
            pressure: latestData.pressure?.toFixed(2) || 0,
            airTemperature: latestData.airTemperature?.toFixed(2) || 0,
            relativeHumidity: latestData.relativeHumidity?.toFixed(2) || 0
          };
          console.log('Processed data:', this.dongshanData);  // 添加调试日志
          
          // 更新图表
          this.$nextTick(() => {
            this.updateCharts(data);
          });
        }
      } catch (error) {
        console.error('获取数据失败:', error);
      }
    },
    
    getLabel(key) {
      const labels = {
        chlorophyll: '叶绿素 a',
        waterTemperature: '水温',
        dissolvedOxygen: '溶解氧',
        pH: 'pH',
        salinity: '盐度',
        pressure: '气压',
        airTemperature: '空气温度',
        relativeHumidity: '相对湿度'
      };
      return labels[key] || key;
    },
    
    updateCharts(data) {
      // 销毁现有的图表
      this.charts.forEach(chart => {
        if (chart) {
          chart.destroy();
        }
      });
      this.charts = [];
      
      // 定义要显示的指标
      const metrics = ['chlorophyll', 'waterTemperature', 'dissolvedOxygen', 'ph', 'salinity', 'pressure', 'airTemperature', 'relativeHumidity'];
      
      // 数据采样间隔（每10个点取一个）
      const samplingInterval = 10;
      const sampledData = data.filter((_, index) => index % samplingInterval === 0);
      
      // 为每个指标创建图表
      metrics.forEach((metric, index) => {
        const canvas = this.$refs['chart' + index][0];
        if (!canvas) return;
        
        const ctx = canvas.getContext('2d');
        // 修改时间格式，只显示日期
        const timestamps = sampledData.map(item => {
          const date = new Date(item.date);
          return date.toLocaleDateString('zh-CN', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit'
          });
        });
        const values = sampledData.map(item => item[metric]);
        
        const chart = new Chart(ctx, {
          type: 'line',
          data: {
            labels: timestamps,
            datasets: [{
              label: this.getLabel(metric),
              data: values,
              borderColor: this.getChartColor(index),
              backgroundColor: 'transparent',
              tension: 0.1,
              fill: false,
              pointRadius: 2,
              pointStyle: 'circle',
              pointBackgroundColor: this.getChartColor(index),
              borderWidth: 2
            }]
          },
          options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
              title: {
                display: true,
                text: this.getLabel(metric),
                font: {
                  size: 16,
                  weight: 'bold'
                },
                padding: {
                  top: 10,
                  bottom: 20
                }
              },
              legend: {
                display: false
              },
              tooltip: {
                mode: 'index',
                intersect: false,
                callbacks: {
                  title: function(tooltipItems) {
                    return tooltipItems[0].label;
                  }
                }
              }
            },
            scales: {
              x: {
                display: true,
                title: {
                  display: true,
                  text: '日期',
                  font: {
                    size: 14,
                    weight: 'bold'
                  }
                },
                ticks: {
                  maxRotation: 45,
                  minRotation: 45,
                  font: {
                    size: 12
                  }
                },
                grid: {
                  display: false
                }
              },
              y: {
                display: true,
                title: {
                  display: true,
                  text: '数值',
                  font: {
                    size: 14,
                    weight: 'bold'
                  }
                },
                beginAtZero: false,
                grid: {
                  color: 'rgba(0, 0, 0, 0.1)'
                }
              }
            },
            interaction: {
              mode: 'nearest',
              axis: 'x',
              intersect: false
            }
          }
        });
        
        this.charts.push(chart);
      });
    },
    
    getChartColor(index) {
      const colors = [
        'rgb(75, 192, 192)',
        'rgb(255, 99, 132)',
        'rgb(54, 162, 235)',
        'rgb(255, 206, 86)',
        'rgb(153, 102, 255)',
        'rgb(255, 159, 64)',
        'rgb(201, 203, 207)'
      ];
      return colors[index % colors.length];
    }
  }
}
</script>
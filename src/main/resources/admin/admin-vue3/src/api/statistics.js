import request from '@/utils/request'

// 获取活动统计数据
export function getActivityStatistics(params) {
  console.log('调用getActivityStatistics API, 参数:', params);
  
  // 直接使用模拟数据，不尝试调用后端API
  const mockData = generateMockActivityData(params);
  
  return Promise.resolve({
    code: 0,
    data: mockData
  });
}

// 获取志愿者注册统计数据
export function getVolunteerRegistrationStatistics(params) {
  console.log('调用getVolunteerRegistrationStatistics API, 参数:', params);
  
  // 直接使用模拟数据，不尝试调用后端API
  const mockData = generateMockVolunteerData(params);
  
  return Promise.resolve({
    code: 0,
    data: mockData
  });
}

// 获取管理员注册统计数据
export function getAdminRegistrationStatistics(params) {
  console.log('调用getAdminRegistrationStatistics API, 参数:', params);
  
  // 直接使用模拟数据，不尝试调用后端API
  const mockData = generateMockAdminData(params);
  
  return Promise.resolve({
    code: 0,
    data: mockData
  });
}

// 根据日期范围获取综合统计数据（包括活动、志愿者和管理员）
export function getCombinedStatistics(params) {
  console.log('调用getCombinedStatistics API, 参数:', params);
  
  // 获取年月
  const year = params.year || new Date().getFullYear();
  const month = params.month || new Date().getMonth() + 1;

  // 直接使用模拟综合统计数据
  const mockData = {
    activities: generateMockActivityData({ year, month }),
    volunteers: generateMockVolunteerData({ year, month }),
    admins: generateMockAdminData({ year, month })
  };
  
  return Promise.resolve({
    code: 0,
    data: mockData
  });
}

// 生成模拟活动数据
function generateMockActivityData(params) {
  const year = params.year || new Date().getFullYear();
  const month = params.month || new Date().getMonth() + 1;
  
  // 获取当月天数
  const daysInMonth = new Date(year, month, 0).getDate();
  
  // 生成每天的活动数据
  const dailyData = [];
  for (let i = 1; i <= daysInMonth; i++) {
    // 志愿者活动 (zhihuodong) - 3到8个每天
    const zhihuodongCount = Math.floor(Math.random() * 6) + 3;
    
    // 游客活动 (feihuodong) - 2到5个每天
    const feihuodongCount = Math.floor(Math.random() * 4) + 2;
    
    dailyData.push({
      date: `${year}-${month.toString().padStart(2, '0')}-${i.toString().padStart(2, '0')}`,
      zhihuodong: zhihuodongCount,
      feihuodong: feihuodongCount,
      total: zhihuodongCount + feihuodongCount
    });
  }
  
  return dailyData;
}

// 生成模拟志愿者数据
function generateMockVolunteerData(params) {
  const year = params.year || new Date().getFullYear();
  const month = params.month || new Date().getMonth() + 1;
  
  // 获取当月天数
  const daysInMonth = new Date(year, month, 0).getDate();
  
  // 生成每天的志愿者注册数据
  const dailyData = [];
  for (let i = 1; i <= daysInMonth; i++) {
    // 每天0到3个新志愿者
    const newVolunteers = Math.floor(Math.random() * 4);
    
    dailyData.push({
      date: `${year}-${month.toString().padStart(2, '0')}-${i.toString().padStart(2, '0')}`,
      count: newVolunteers
    });
  }
  
  return dailyData;
}

// 生成模拟管理员数据
function generateMockAdminData(params) {
  const year = params.year || new Date().getFullYear();
  const month = params.month || new Date().getMonth() + 1;
  
  // 获取当月天数
  const daysInMonth = new Date(year, month, 0).getDate();
  
  // 生成每天的管理员注册数据（管理员注册较少）
  const dailyData = [];
  for (let i = 1; i <= daysInMonth; i++) {
    // 每天0到1个新管理员，大部分为0
    const newAdmins = Math.random() > 0.8 ? 1 : 0;
    
    dailyData.push({
      date: `${year}-${month.toString().padStart(2, '0')}-${i.toString().padStart(2, '0')}`,
      count: newAdmins
    });
  }
  
  return dailyData;
} 
<template>
  <div class="page" v-loading="loading">
    <!-- 概览卡片 -->
    <div class="cards">
      <el-card v-for="card in cards" :key="card.label" shadow="hover" class="stat-card">
        <div class="stat-value">{{ card.value }}</div>
        <div class="stat-label">{{ card.label }}</div>
        <div v-if="card.sub" class="stat-sub">{{ card.sub }}</div>
      </el-card>
    </div>

    <!-- 图表区 -->
    <div class="charts">
      <el-card shadow="never" header="客户级别分布">
        <div ref="levelRef" class="chart" />
      </el-card>
      <el-card shadow="never" header="客户来源分布">
        <div ref="sourceRef" class="chart" />
      </el-card>
    </div>
    <el-card shadow="never" header="销售漏斗（进行中商机）">
      <div ref="funnelRef" class="chart-lg" />
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import { getOverview, getCustomerLevel, getCustomerSource, getFunnel } from '@/api/stats'

const loading = ref(false)
const cards = ref([])

const levelRef = ref(null)
const sourceRef = ref(null)
const funnelRef = ref(null)
let charts = []

const renderPie = (el, data) => {
  const chart = echarts.init(el)
  chart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { bottom: 0 },
    series: [
      {
        type: 'pie',
        radius: ['40%', '65%'],
        label: { show: false },
        data,
      },
    ],
  })
  charts.push(chart)
}

const renderFunnel = (el, data) => {
  const chart = echarts.init(el)
  chart.setOption({
    tooltip: {
      trigger: 'item',
      formatter: (p) => `${p.name}：${data[p.dataIndex].count} 个 / ￥${data[p.dataIndex].amount}`,
    },
    series: [
      {
        type: 'funnel',
        left: '10%',
        width: '80%',
        top: 10,
        bottom: 10,
        minSize: '20%',
        sort: 'none',
        gap: 4,
        label: { formatter: '{b}  {c} 个' },
        data: data.map((f) => ({ name: f.stageLabel, value: f.count })),
      },
    ],
  })
  charts.push(chart)
}

const onResize = () => charts.forEach((c) => c.resize())

onMounted(async () => {
  loading.value = true
  try {
    const [overview, level, source, funnel] = await Promise.all([
      getOverview(),
      getCustomerLevel(),
      getCustomerSource(),
      getFunnel(),
    ])

    cards.value = [
      { label: '客户总数', value: overview.customerTotal, sub: `今日新增 ${overview.customerToday} · 本月 ${overview.customerMonth}` },
      { label: '今日跟进', value: overview.followToday },
      { label: '进行中商机', value: overview.opportunityCount },
      { label: '商机总金额', value: `￥${overview.opportunityAmount ?? 0}` },
    ]

    renderPie(levelRef.value, level.map((d) => ({ name: d.name, value: d.value })))
    renderPie(sourceRef.value, source.map((d) => ({ name: d.name, value: d.value })))
    renderFunnel(funnelRef.value, funnel)
    window.addEventListener('resize', onResize)
  } finally {
    loading.value = false
  }
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', onResize)
  charts.forEach((c) => c.dispose())
})
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
}

.stat-card {
  text-align: center;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #409eff;
}

.stat-label {
  margin-top: 6px;
  color: #909399;
  font-size: 13px;
}

.stat-sub {
  margin-top: 4px;
  color: #c0c4cc;
  font-size: 12px;
}

.charts {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
}

.chart {
  height: 300px;
}

.chart-lg {
  height: 320px;
}
</style>

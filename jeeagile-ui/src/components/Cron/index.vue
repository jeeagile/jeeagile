<style lang="less" scoped>
  #changeTab {
    .el-tabs {
      box-shadow: none;
    }

    .tabBody {
      height: 170px;
      overflow: auto;
    }

    .tabBody {
      .el-row {
        margin: 10px 0;

        .long {
          .el-select {
            width: 260px;
          }
        }

        .el-input-number {
          width: 120px;
        }
      }
    }

    .bottom {
      width: 100%;
      text-align: center;
      margin-top: 5px;
      position: relative;

      .value {
        font-size: 18px;
        vertical-align: middle;
      }
    }
  }
</style>
<template>
  <div id="changeTab">
    <el-tabs type="border-card">
      <el-tab-pane>
        <span slot="label"><i class="el-icon-date"></i>秒 </span>
        <div class="tabBody">
          <el-row>
            <el-radio v-model="second.cronEvery" label="1">每一秒钟</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="second.cronEvery" label="2">每隔
              <el-input-number size="small" v-model="second.incrementIncrement" :min="1" :max="60"></el-input-number>
              秒执行 从
              <el-input-number size="small" v-model="second.incrementStart" :min="0" :max="59"></el-input-number>
              秒开始
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="second.cronEvery" label="3">具体秒数(可多选)
              <el-select size="small" multiple v-model="second.specificSpecific">
                <el-option v-for="val in 60" :key="'sc' + val" :value="val-1">{{val-1}}</el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="second.cronEvery" label="4">周期从
              <el-input-number size="small" v-model="second.rangeStart" :min="1" :max="60"></el-input-number>
              到
              <el-input-number size="small" v-model="second.rangeEnd" :min="0" :max="59"></el-input-number>
              秒
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label"><i class="el-icon-date"></i> 分 </span>
        <div class="tabBody">
          <el-row>
            <el-radio v-model="minute.cronEvery" label="1">每一分钟</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="minute.cronEvery" label="2">每隔
              <el-input-number size="small" v-model="minute.incrementIncrement" :min="1" :max="60"></el-input-number>
              分执行 从
              <el-input-number size="small" v-model="minute.incrementStart" :min="0" :max="59"></el-input-number>
              分开始
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="minute.cronEvery" label="3">具体分钟数(可多选)
              <el-select size="small" multiple v-model="minute.specificSpecific">
                <el-option v-for="val in 60" :key="'m' + val" :value="val-1">{{val-1}}</el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="minute.cronEvery" label="4">周期从
              <el-input-number size="small" v-model="minute.rangeStart" :min="1" :max="60"></el-input-number>
              到
              <el-input-number size="small" v-model="minute.rangeEnd" :min="0" :max="59"></el-input-number>
              分
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label"><i class="el-icon-date"></i> 时 </span>
        <div class="tabBody">
          <el-row>
            <el-radio v-model="hour.cronEvery" label="1">每一小时</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="hour.cronEvery" label="2">每隔
              <el-input-number size="small" v-model="hour.incrementIncrement" :min="0" :max="23"></el-input-number>
              小时执行 从
              <el-input-number size="small" v-model="hour.incrementStart" :min="0" :max="23"></el-input-number>
              小时开始
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="hour.cronEvery" label="3">具体小时数(可多选)
              <el-select size="small" multiple v-model="hour.specificSpecific">
                <el-option v-for="val in 24" :key="'h' + val" :value="val-1">{{val-1}}</el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="hour.cronEvery" label="4">周期从
              <el-input-number size="small" v-model="hour.rangeStart" :min="0" :max="23"></el-input-number>
              到
              <el-input-number size="small" v-model="hour.rangeEnd" :min="0" :max="23"></el-input-number>
              小时
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label"><i class="el-icon-date"></i> 天 </span>
        <div class="tabBody">
          <el-row>
            <el-radio v-model="day.cronEvery" label="1">每一天</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="day.cronEvery" label="3">每隔
              <el-input-number size="small" v-model="day.incrementIncrement" :min="1" :max="31"></el-input-number>
              天执行 从
              <el-input-number size="small" v-model="day.incrementStart" :min="1" :max="31"></el-input-number>
              天开始
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="day.cronEvery" label="5">具体天数(可多选)
              <el-select size="small" multiple v-model="day.specificSpecific">
                <el-option v-for="val in 31" :key="'md' + val" :value="val">{{val}}</el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="day.cronEvery" label="8">在这个月的最后一个
              <el-select size="small" v-model="day.cronLastSpecificDomDay">
                <el-option v-for="val in 7" :key="'lw' + val"
                           :label="['星期天', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][val-1]" :value="val"></el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="day.cronEvery" label="10">最近的工作日（周一至周五）至本月
              <el-input-number size="small" v-model="day.cronDaysNearestWeekday" :min="1" :max="31"></el-input-number>
              日
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="day.cronEvery" label="6">在这个月的最后一天</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="day.cronEvery" label="7">在这个月的最后一个工作日</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="day.cronEvery" label="9">
              在本月底前
              <el-input-number size="small" v-model="day.cronDaysBeforeEomMinus" :min="1" :max="31"></el-input-number>
              天
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label"><i class="el-icon-date"></i> 月 </span>
        <div class="tabBody">
          <el-row>
            <el-radio v-model="month.cronEvery" label="1">每一月</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="month.cronEvery" label="2">每隔
              <el-input-number size="small" v-model="month.incrementIncrement" :min="0" :max="12"></el-input-number>
              月执行 从
              <el-input-number size="small" v-model="month.incrementStart" :min="0" :max="12"></el-input-number>
            </el-radio>
            月开始
          </el-row>
          <el-row>
            <el-radio class="long" v-model="month.cronEvery" label="3">具体月数(可多选)
              <el-select size="small" multiple v-model="month.specificSpecific">
                <el-option v-for="val in 12" :key="'mt' + val" :label="val" :value="val"></el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="month.cronEvery" label="4">从
              <el-input-number size="small" v-model="month.rangeStart" :min="1" :max="12"></el-input-number>
              到
              <el-input-number size="small" v-model="month.rangeEnd" :min="1" :max="12"></el-input-number>
              月之间的每个月
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label"><i class="el-icon-date"></i> 周 </span>
        <div class="tabBody">
          <el-row>
            <el-radio v-model="day.cronEvery" label="2">每隔
              <el-input-number size="small" v-model="week.incrementIncrement" :min="1" :max="7"></el-input-number>
              周执行 从
              <el-select size="small" v-model="week.incrementStart" style="width: 120px">
                <el-option v-for="val in 7" :key="'d' + val"
                           :label="['星期天', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][val-1]"
                           :value="val"></el-option>
              </el-select>
              开始
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="day.cronEvery" label="4">具体星期几(可多选)
              <el-select size="small" multiple v-model="week.specificSpecific">
                <el-option v-for="val in 7"
                           :key="'dc' + val"
                           :label="['星期天', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][val-1]"
                           :value="['SUN','MON','TUE','WED','THU','FRI','SAT'][val-1]"
                ></el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="day.cronEvery" label="11">在这个月的第
              <el-input-number size="small" v-model="week.cronNthDayNth" :min="1" :max="5"></el-input-number>
              个
              <el-select size="small" v-model="week.cronNthDayDay" style="width: 120px">
                <el-option v-for="val in 7" :key="'wd' + val"
                           :label="['星期天', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'][val-1]" :value="val"></el-option>
              </el-select>

            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label"><i class="el-icon-date"></i> 年 </span>
        <div class="tabBody">
          <el-row>
            <el-radio v-model="year.cronEvery" label="1">每一年</el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="year.cronEvery" label="2">每隔
              <el-input-number size="small" v-model="year.incrementIncrement" :min="1" :max="99"></el-input-number>
              年执行 从
              <el-input-number size="small" v-model="year.incrementStart" :min="2020" :max="2120"></el-input-number>
              年开始
            </el-radio>
          </el-row>
          <el-row>
            <el-radio class="long" v-model="year.cronEvery" label="3">具体年份(可多选)
              <el-select size="small" filterable multiple v-model="year.specificSpecific">
                <el-option v-for="val in 100" :key="'y' + val" :label="2020+val" :value="2020+val"></el-option>
              </el-select>
            </el-radio>
          </el-row>
          <el-row>
            <el-radio v-model="year.cronEvery" label="4">从
              <el-input-number size="small" v-model="year.rangeStart" :min="2020" :max="2120"></el-input-number>
              到
              <el-input-number size="small" v-model="year.rangeEnd" :min="2020" :max="2120"></el-input-number>
              年之间的每一年
            </el-radio>
          </el-row>
        </div>
      </el-tab-pane>
    </el-tabs>
    <el-table :data="tableCron" size="mini" border :header-cell-style="{textAlign: 'center'}">
      <el-table-column prop="secondsText" label="秒" width="72px"></el-table-column>
      <el-table-column prop="minutesText" label="分" width="72px"></el-table-column>
      <el-table-column prop="hoursText" label="时" width="73px"></el-table-column>
      <el-table-column prop="daysText" label="日" width="73px"></el-table-column>
      <el-table-column prop="monthsText" label="月" width="73px"></el-table-column>
      <el-table-column prop="weeksText" label="周" width="72px"></el-table-column>
      <el-table-column prop="yearsText" label="年" width="72px"></el-table-column>
    </el-table>
    <div class="bottom">
      <el-button type="primary" @click="change">确定</el-button>
      <el-button type="primary" @click="close">关闭</el-button>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'VueCron',
    model: { prop: 'cronValue' },
    props: ['data', 'cronValue'],
    data() {
      return {
        second: {
          cronEvery: '',
          incrementStart: '3',
          incrementIncrement: '5',
          rangeStart: '',
          rangeEnd: '',
          specificSpecific: [],
          intervalRule: /^([0-9]|[1-5]\d)\/([1-9]|[1-5]\d|60)$/,
          arrayRule: /^(([0-9]|[1-5]\d),)*([0-9]|[1-5]\d)$/,
          cycleRule: /^([1-9]|[1-5]\d|60)-([0-9]|[1-5]\d)$/
        },
        minute: {
          cronEvery: '',
          incrementStart: '3',
          incrementIncrement: '5',
          rangeStart: '',
          rangeEnd: '',
          specificSpecific: [],
          intervalRule: /^([0-9]|[1-5]\d)\/([1-9]|[1-5]\d|60)$/,
          arrayRule: /^(([0-9]|[1-5]\d),)*([0-9]|[1-5]\d)$/,
          cycleRule: /^([1-9]|[1-5]\d|60)-([0-9]|[1-5]\d)$/
        },
        hour: {
          cronEvery: '',
          incrementStart: '3',
          incrementIncrement: '5',
          rangeStart: '',
          rangeEnd: '',
          specificSpecific: [],
          intervalRule: /^([0-9]|1\d|2[0-3])\/([0-9]|1\d|2[0-3])$/,
          arrayRule: /^(([0-9]|1\d|2[0-3]),)*([0-9]|1\d|2[0-3])$/,
          cycleRule: /^([0-9]|1\d|2[0-3])-([0-9]|1\d|2[0-3])$/
        },
        day: {
          cronEvery: '',
          incrementStart: '',
          incrementIncrement: '1',
          rangeStart: '',
          rangeEnd: '',
          specificSpecific: [],
          cronLastSpecificDomDay: 1,
          cronDaysBeforeEomMinus: '',
          cronDaysNearestWeekday: '',
          intervalRule: /^([1-9]|[1-2]\d|3[0-1])\/([1-9]|[1-2]\d|3[0-1])$/,
          arrayRule: /^(([1-9]|[1-2]\d|3[0-1]),)*([1-9]|[1-2]\d|3[0-1])$/,
          cycleRule: /^([1-9]|[1-2]\d|3[0-1])-([1-9]|[1-2]\d|3[0-1])$/
        },
        week: {
          cronEvery: '',
          incrementStart: '1',
          incrementIncrement: '1',
          specificSpecific: [],
          cronNthDayDay: 1,
          cronNthDayNth: '1',
          arrayRule: /^((SUN|MON|TUE|WED|THU|FRI|SAT),)*(SUN|MON|TUE|WED|THU|FRI|SAT)$/,
          intervalRule: /^[1-7]\/[1-7]$/
        },
        month: {
          cronEvery: '',
          incrementStart: '3',
          incrementIncrement: '5',
          rangeStart: '',
          rangeEnd: '',
          specificSpecific: [],
          intervalRule: /^(\d|1[0-2])\/(\d|1[0-2])$/,
          arrayRule: /^(([1-9]|1[0-2]),)*([1-9]|1[0-2])$/,
          cycleRule: /^([1-9]|1[0-2])-([1-9]|1[0-2])$/
        },
        year: {
          cronEvery: '',
          incrementStart: '2020',
          incrementIncrement: '1',
          rangeStart: '',
          rangeEnd: '',
          specificSpecific: [],
          intervalRule: /^(20[2-9]\d|21[0-1]\d|2120)\/([1-9]|[1-9]\d)$/,
          arrayRule: /^((20[2-9]\d|21[0-1]\d|2120),)*(20[2-9]\d|21[0-1]\d|2120)$/,
          cycleRule: /^(20[2-9]\d|21[0-1]\d|2120)-(20[2-9]\d|21[0-1]\d|2120)$/
        },
        output: {
          second: '',
          minute: '',
          hour: '',
          day: '',
          month: '',
          week: '',
          year: ''
        }
      }
    },
    watch: {
      data() {
        this.rest(this.$data)
      },
      cronValue(val) {
        this.changeTime(val)
      },
      output: {
        handler(curVal, oldVal) {
          for (const key in curVal) {
            this.verifyAndConvert(key)
          }
        },
        deep: true
      }
    },
    computed: {
      secondsText() {
        let seconds = ''
        let cronEvery = this.second.cronEvery
        switch (cronEvery.toString()) {
        case '1':
          seconds = '*'
          break
        case '2':
          seconds = this.second.incrementStart + '/' + this.second.incrementIncrement
          break
        case '3':
          this.second.specificSpecific.map(val => {
            seconds += val + ','
          })
          seconds = seconds.slice(0, -1)
          break
        case '4':
          seconds = this.second.rangeStart + '-' + this.second.rangeEnd
          break
        }
        return seconds
      },
      minutesText() {
        let minutes = ''
        let cronEvery = this.minute.cronEvery
        switch (cronEvery.toString()) {
        case '1':
          minutes = '*'
          break
        case '2':
          minutes = this.minute.incrementStart + '/' + this.minute.incrementIncrement
          break
        case '3':
          this.minute.specificSpecific.map(val => {
            minutes += val + ','
          })
          minutes = minutes.slice(0, -1)
          break
        case '4':
          minutes = this.minute.rangeStart + '-' + this.minute.rangeEnd
          break
        }
        return minutes
      },
      hoursText() {
        let hours = ''
        let cronEvery = this.hour.cronEvery
        switch (cronEvery.toString()) {
        case '1':
          hours = '*'
          break
        case '2':
          hours = this.hour.incrementStart + '/' + this.hour.incrementIncrement
          break
        case '3':
          this.hour.specificSpecific.map(val => {
            hours += val + ','
          })
          hours = hours.slice(0, -1)
          break
        case '4':
          hours = this.hour.rangeStart + '-' + this.hour.rangeEnd
          break
        }
        return hours
      },
      daysText() {
        let days = ''
        let cronEvery = this.day.cronEvery
        switch (cronEvery.toString()) {
        case '1':
          break
        case '2':
        case '4':
        case '11':
          days = '?'
          break
        case '3':
          days = this.day.incrementStart + '/' + this.day.incrementIncrement
          break
        case '5':
          this.day.specificSpecific.map(val => {
            days += val + ','
          })
          days = days.slice(0, -1)
          break
        case '6':
          days = 'L'
          break
        case '7':
          days = 'LW'
          break
        case '8':
          days = this.day.cronLastSpecificDomDay + 'L'
          break
        case '9':
          days = 'L-' + this.day.cronDaysBeforeEomMinus
          break
        case '10':
          days = this.day.cronDaysNearestWeekday + 'W'
          break
        }
        return days
      },
      weeksText() {
        let weeks = ''
        let cronEvery = this.day.cronEvery
        switch (cronEvery.toString()) {
        case '1':
        case '3':
        case '5':
          weeks = '?'
          break
        case '2':
          weeks = this.week.incrementStart + '/' + this.week.incrementIncrement
          break
        case '4':
          this.week.specificSpecific.map(val => {
            weeks += val + ','
          })
          weeks = weeks.slice(0, -1)
          break
        case '6':
        case '7':
        case '8':
        case '9':
        case '10':
          weeks = '?'
          break
        case '11':
          weeks = this.week.cronNthDayDay + '#' + this.week.cronNthDayNth
          break
        }
        return weeks
      },
      monthsText() {
        let months = ''
        let cronEvery = this.month.cronEvery
        switch (cronEvery.toString()) {
        case '1':
          months = '*'
          break
        case '2':
          months = this.month.incrementStart + '/' + this.month.incrementIncrement
          break
        case '3':
          this.month.specificSpecific.map(val => {
            months += val + ','
          })
          months = months.slice(0, -1)
          break
        case '4':
          months = this.month.rangeStart + '-' + this.month.rangeEnd
          break
        }
        return months
      },
      yearsText() {
        let years = ''
        let cronEvery = this.year.cronEvery
        switch (cronEvery.toString()) {
        case '1':
          years = '*'
          break
        case '2':
          years = this.year.incrementStart + '/' + this.year.incrementIncrement
          break
        case '3':
          this.year.specificSpecific.map(val => {
            years += val + ','
          })
          years = years.slice(0, -1)
          break
        case '4':
          years = this.year.rangeStart + '-' + this.year.rangeEnd
          break
        }
        return years
      },
      tableCron() {
        return [{
          secondsText: this.secondsText || '*',
          minutesText: this.minutesText || '*',
          hoursText: this.hoursText || '*',
          daysText: this.daysText || '*',
          monthsText: this.monthsText || '*',
          weeksText: this.weeksText || '*',
          yearsText: this.yearsText || '*'
        }]
      },
      cron() {
        return `${this.secondsText || '*'} ${this.minutesText || '*'} ${this.hoursText || '*'} ${this.daysText || '*'} ${this.monthsText || '*'} ${this.weeksText || '?'} ${this.yearsText || '*'}`
      }
    },
    methods: {
      getValue() {
        return this.cron
      },
      change() {
        this.$emit('change', this.cron)
        this.close()
      },
      close() {
        this.$emit('close')
      },
      rest(data) {
        for (let i in data) {
          if (!(data[i] instanceof RegExp)) {
            if (data[i] instanceof Object) {
              this.rest(data[i])
            } else {
              switch (typeof data[i]) {
              case 'object':
                data[i] = []
                break
              case 'string':
                data[i] = ''
                break
              }
            }
          }
        }
      },
      changeTime(newValue) {
        if (!newValue || newValue.trim().length < 11) {
          this.showError()
          return
        }
        const arr = newValue.trim().split(' ')
        if (arr.length !== 6 && arr.length !== 7) {
          this.showError()
          return
        }
        this.output.second = arr[0]
        this.output.minute = arr[1]
        this.output.hour = arr[2]
        this.output.day = arr[3]
        this.output.month = arr[4]
        this.output.week = arr[5]
        this.output.year = arr.length === 7 ? arr[6] : ''
      },
      showError() {
        this.$message.error('格式错误')
      },
      verifyAndConvert(key) {
        const tag = this[key]
        const value = this.output[key]
        if (value === '?') return
        if (value === '*') {
          tag.cronEvery = '1'
          if (key === 'week') {
            this.day.cronEvery = '4'
            tag.specificSpecific = []
          }
          return
        }
        if (value === 'L') {
          tag.cronEvery = '6'
          return
        }
        if (value === 'LW') {
          tag.cronEvery = '7'
          return
        }
        if (tag.intervalRule.test(value)) {
          const arr = value.trim().split('/')
          tag.cronEvery = key === 'day' ? '3' : '2'
          if (key === 'week') this.day.cronEvery = '2'
          else tag.cronEvery = key === 'day' ? '3' : '2'
          tag.incrementIncrement = parseInt(arr[1])
          tag.incrementStart = parseInt(arr[0])
          return
        }
        if (tag.arrayRule.test(value)) {
          const arr = value.trim().split(',')
          tag.specificSpecific = []
          if (key === 'week') {
            this.day.cronEvery = '4'
            arr.forEach(item => {
              tag.specificSpecific.push(item)
            })
            return
          }
          tag.cronEvery = key === 'day' ? '5' : '3'
          arr.forEach(item => {
            if (item && item !== '') {
              tag.specificSpecific.push(parseInt(item))
            }
          })
          return
        }
        if (key !== 'week' && tag.cycleRule.test(value)) {
          const arr = value.trim().split('-')
          tag.cronEvery = '4'
          tag.rangeStart = parseInt(arr[0])
          tag.rangeEnd = parseInt(arr[1])
          return
        }
        if (key === 'day') {
          if (/^[1-7]L$/.test(value)) {
            tag.cronEvery = '8'
            tag.cronLastSpecificDomDay = parseInt(value.charAt(0))
            return
          }
          if (/^L-([1-9]|[1-2]\d|3[0-1])$/.test(value)) {
            tag.cronEvery = '9'
            tag.cronDaysBeforeEomMinus = parseInt(value.trim().split('-')[1])
            return
          }
          if (/^([1-9]|[1-2]\d|3[0-1])W$/.test(value)) {
            tag.cronEvery = '10'
            tag.cronDaysNearestWeekday = parseInt(value.substr(0, value.length - 1))
            return
          }
        }
        if (key === 'week' && /^[1-7]#[1-5]$/.test(value)) {
          const arr = value.trim().split('#')
          this.day.cronEvery = '11'
          tag.cronNthDayNth = arr[1]
          tag.cronNthDayDay = arr[0]
          return
        }
        this.showError()
      }
    },
    created() {
      this.changeTime(this.cronValue)
    }
  }</script>

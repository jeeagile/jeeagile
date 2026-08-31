import Vue from 'vue'
import { AgileBaseDict } from './index'

const DroolsModelType = new AgileBaseDict('对象类型', [
  {
    value: 'java',
    label: 'JAVA',
    symbol: 'JAVA',
    tag: 'success'
  },
  {
    value: 'declare',
    label: 'DECLARE',
    symbol: 'DECLARE',
    tag: 'primary'
  }
])
Vue.prototype.DroolsModelType = DroolsModelType


const DroolsFieldType = new AgileBaseDict('字段类型', [
  {
    value: 'String',
    label: 'String',
    symbol: 'String'
  },
  {
    value: 'BigDecimal',
    label: 'BigDecimal',
    symbol: 'BigDecimal'
  },
  {
    value: 'BigInteger',
    label: 'BigInteger',
    symbol: 'BigInteger'
  },
  {
    value: '',
    label: 'Boolean',
    symbol: 'Boolean'
  },
  {
    value: 'Date',
    label: 'Date',
    symbol: 'Date'
  },
  {
    value: 'Double',
    label: 'Double',
    symbol: 'Double'
  },
  {
    value: 'Float',
    label: 'Float',
    symbol: 'Float'
  },
  {
    value: 'Integer',
    label: 'Integer',
    symbol: 'Integer'
  },
  {
    value: 'LocalDate',
    label: 'LocalDate',
    symbol: 'LocalDate'
  },
  {
    value: 'LocalDateTime',
    label: 'LocalDateTime',
    symbol: 'LocalDateTime'
  },
  {
    value: 'LocalTime',
    label: 'LocalTime',
    symbol: 'LocalTime'
  },
  {
    value: 'Long',
    label: 'Long',
    symbol: 'Long'
  },
  {
    value: 'Short',
    label: 'Short',
    symbol: 'Short'
  },
  {
    value: 'boolean',
    label: 'boolean',
    symbol: 'boolean'
  },
  {
    value: 'byte',
    label: 'byte',
    symbol: 'byte'
  },
  {
    value: 'char',
    label: 'char',
    symbol: 'char'
  },
  {
    value: 'double',
    label: 'double',
    symbol: 'double'
  },
  {
    value: 'float',
    label: 'float',
    symbol: 'float'
  },
  {
    value: 'int',
    label: 'int',
    symbol: 'int'
  },
  {
    value: 'long',
    label: 'long',
    symbol: 'long'
  },
  {
    value: 'short',
    label: 'short',
    symbol: 'short'
  },
  {
    value: 'Map',
    label: 'Map',
    symbol: 'Map'
  },
  {
    value: 'Object',
    label: '数据对象',
    symbol: 'Object'
  }
])
Vue.prototype.DroolsFieldType = DroolsFieldType


const DroolsRuleType = new AgileBaseDict('规则类型', [
  {
    value: '01',
    label: 'Drl File',
    symbol: 'DRL_FILE',
    tag: 'success'
  },
  {
    value: '02',
    label: 'Guided Rule',
    symbol: 'GUIDED_RULE',
    tag: 'primary'
  },
  {
    value: '03',
    label: 'Score Card',
    symbol: 'SCORE_CARD',
    tag: 'primary'
  }
])
Vue.prototype.DroolsRuleType = DroolsRuleType

export {
  DroolsModelType,
  DroolsFieldType,
  DroolsRuleType
}

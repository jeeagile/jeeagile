<template>
  <div>
    <svg-icon icon-class="github" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'JeeAgileGit',
  data() {
    return {
      url: 'https://gitee.com/jeeagile/jeeagile'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>

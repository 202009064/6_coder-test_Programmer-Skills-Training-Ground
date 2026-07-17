import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useLevelStore = defineStore('level', () => {
  const currentLevel = ref(null)
  const selectedOptions = ref([])

  function setLevel(level) {
    currentLevel.value = level
    selectedOptions.value = []
  }

  function addOption(option) {
    if (!selectedOptions.value.includes(option)) {
      selectedOptions.value.push(option)
    }
  }

  function removeOption(option) {
    selectedOptions.value = selectedOptions.value.filter(o => o !== option)
  }

  function clearLevel() {
    currentLevel.value = null
    selectedOptions.value = []
  }

  return {
    currentLevel,
    selectedOptions,
    setLevel,
    addOption,
    removeOption,
    clearLevel,
  }
})
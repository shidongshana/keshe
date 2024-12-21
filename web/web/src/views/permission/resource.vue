<template>
  <div>
    <form @submit.prevent="uploadFile">
      <input type="file" @change="handleFileChange" />
      <button type="submit">上传图片</button>
    </form>
    <div v-if="imageUrl">
      <img :src="imageUrl" alt="Uploaded Image" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

const file = ref(null);
const imageUrl = ref('');

const handleFileChange = (event) => {
  file.value = event.target.files[0];
};

const uploadFile = async () => {
  const formData = new FormData();
  formData.append('file', file.value);

  try {
    const response = await axios.post('http://localhost:8081/api/fileoss/uploadOssFile', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
        'Authorization': `Bearer ${JSON.parse(localStorage.getItem('credentials'))?.token}`
      },
    });
    imageUrl.value = response.data;
  } catch (error) {
    console.error('Error uploading file:', error);
  }
};
</script>

<style scoped>
/* 添加一些样式 */
</style>
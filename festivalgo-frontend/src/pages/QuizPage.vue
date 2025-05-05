<template>
    <div class="quiz-page">
      <h2 class="text-2xl font-bold mb-4">Quiz de recomandare muzicală 🎵</h2>
  
      <div v-if="questions.length && !recommendedGenre">
        <form @submit.prevent="submitQuiz">
          <div
            v-for="(question, qIndex) in questions"
            :key="question.id"
            class="question-box"
          >
            <p class="font-semibold mb-2">
              {{ qIndex + 1 }}. {{ question.questionText }}
            </p>
            <div v-for="answer in question.answers" :key="answer.id" class="ml-2 mb-1">
              <label>
                <input
                  type="radio"
                  :name="'question_' + question.id"
                  :value="answer.id"
                  v-model="selectedAnswers[question.id]"
                  required
                />
                {{ answer.answerText }}
              </label>
            </div>
          </div>
  
          <button type="submit" class="btn">Trimite quiz-ul</button>
        </form>
      </div>
  
      <div v-else-if="!recommendedGenre">
        <p>Se încarcă întrebările...</p>
      </div>
  
      <!-- GENUL RECOMANDAT ANIMAT -->
      <div v-if="recommendedGenre" class="result-screen">
        <p class="text-lg font-semibold mb-2">🎧 Genul recomandat pentru tine este:</p>
        <transition name="fade-scale">
          <div class="genre-animated">{{ recommendedGenre }}</div>
        </transition>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRouter } from 'vue-router'
  import axios from 'axios'
  
  const questions = ref([])
  const selectedAnswers = ref({})
  const recommendedGenre = ref(null)
  const userId = localStorage.getItem('userId')
  const router = useRouter()
  
  onMounted(async () => {
    try {
      const res = await axios.get('http://localhost:8081/api/quiz/questions')
      questions.value = res.data
    } catch (err) {
      console.error('Eroare la încărcarea întrebărilor:', err)
    }
  })
  
  const submitQuiz = async () => {
    const payload = Object.values(selectedAnswers.value).map(id => ({ answerId: id }))
    try {
      const res = await axios.post(`http://localhost:8081/api/recommendation/calculate/${userId}`, payload)
      recommendedGenre.value = res.data
  
      // După 4 secunde, redirecționează către pagina cu festivaluri
      setTimeout(() => {
        router.push('/festivals')
      }, 4000)
    } catch (err) {
      console.error('Eroare la trimiterea răspunsurilor:', err)
    }
  }
  </script>
  
  <style scoped>
  .quiz-page {
    max-width: 700px;
    margin: auto;
    padding: 2rem;
    background-color: #f9f9f9;
  }
  
  .question-box {
    background-color: white;
    border: 2px solid #9333ea;
    border-radius: 10px;
    padding: 16px;
    margin-bottom: 20px;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  }
  
  .btn {
    background-color: #2563eb;
    color: white;
    padding: 0.5rem 1rem;
    border: none;
    border-radius: 6px;
    cursor: pointer;
  }
  .btn:hover {
    background-color: #1e40af;
  }
  
  /* REZULTAT ANIMAT */
  .result-screen {
    text-align: center;
    margin-top: 60px;
  }
  
  .genre-animated {
    font-size: 3rem;
    font-weight: bold;
    color: #9333ea;
    animation: pop 0.6s ease-in-out;
  }
  
  /* animație de scalare */
  @keyframes pop {
    0% {
      transform: scale(0.6);
      opacity: 0;
    }
    100% {
      transform: scale(1);
      opacity: 1;
    }
  }
  
  /* fade + scalare (pentru <transition>) */
  .fade-scale-enter-active {
    transition: all 0.5s ease;
  }
  .fade-scale-enter-from {
    opacity: 0;
    transform: scale(0.8);
  }
  .fade-scale-enter-to {
    opacity: 1;
    transform: scale(1);
  }
  </style>
  
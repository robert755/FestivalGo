<template>
    <div class="p-6">
      <h2 class="text-3xl font-bold mb-6 text-center">Administrare Întrebări</h2>
  
      <!-- Formular adăugare întrebare -->
      <div class="bg-white rounded-2xl shadow p-6 mb-10 max-w-3xl mx-auto">
        <h3 class="text-xl font-semibold mb-4">Adaugă întrebare nouă</h3>
  
        <input
          v-model="newQuestion.questionText"
          placeholder="Scrie textul întrebării"
          class="w-full p-3 border rounded-xl mb-4"
        />
  
        <div v-for="(answer, index) in newQuestion.answers" :key="index" class="flex items-center gap-3 mb-3">
          <input
            v-model="answer.answerText"
            placeholder="Text răspuns"
            class="flex-1 p-2 border rounded-lg"
          />
          <input
            v-model.number="answer.score"
            type="number"
            placeholder="Scor"
            class="w-24 p-2 border rounded-lg"
          />
          <button @click="removeAnswer(index)" class="text-red-500 hover:underline">❌</button>
        </div>
  
        <div class="flex gap-4 mt-4">
          <button @click="addAnswer" class="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-xl">
            ➕ Adaugă răspuns
          </button>
          <button @click="submitQuestion" class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-xl">
            ✅ Salvează întrebarea
          </button>
        </div>
      </div>
  
      <!-- Lista întrebări -->
      <div class="bg-white rounded-2xl shadow p-6 max-w-4xl mx-auto">
        <h3 class="text-xl font-semibold mb-4">Întrebări existente</h3>
  
        <div v-if="questions.length === 0" class="text-gray-500">Nu există întrebări.</div>
  
        <div v-else>
          <div
            v-for="question in questions"
            :key="question.id"
            class="mb-6 border-b pb-4"
          >
            <p class="font-semibold text-lg mb-2">{{ question.questionText }}</p>
            <ul class="list-disc ml-6 text-gray-700">
              <li
                v-for="answer in question.answers"
                :key="answer.id"
              >
                {{ answer.answerText }} — scor: {{ answer.score }}
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    name: 'QuestionAdmin',
    data() {
      return {
        questions: [],
        newQuestion: {
          questionText: '',
          answers: []
        }
      };
    },
    methods: {
      getQuestions() {
        axios.get('http://localhost:8081/api/quiz/questions')
    .then(response => {
      console.log("Date primite de la backend:", response.data); // AICI vezi structura
      this.questions = response.data;
    })
    .catch(error => {
      console.error("Eroare la preluare întrebări:", error);
    });
      },
      addAnswer() {
        this.newQuestion.answers.push({ answerText: '', score: 0 });
      },
      removeAnswer(index) {
        this.newQuestion.answers.splice(index, 1);
      },
      submitQuestion() {
        axios.post('http://localhost:8081/api/quiz/questions', this.newQuestion)
          .then(() => {
            this.newQuestion = { questionText: '', answers: [] };
            this.getQuestions();
          })
          .catch(error => {
            console.error("Eroare la salvare întrebare:", error);
          });
      }
    },
    mounted() {
      this.getQuestions();
    }
  };
  </script>
  
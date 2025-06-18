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

      <div
        v-for="(answer, index) in newQuestion.answers"
        :key="index"
        class="flex items-center gap-3 mb-3"
      >
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
          <div class="flex justify-between items-center">
            <p class="font-semibold text-lg mb-2">{{ question.questionText }}</p>
            <button
              @click="deleteQuestion(question.id)"
              class="text-red-600 hover:text-red-800 text-sm"
            >
              🗑️ Șterge
            </button>
          </div>
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
    },
    deleteQuestion(id) {
      if (confirm("Sigur vrei să ștergi această întrebare?")) {
        axios.delete(`http://localhost:8081/api/quiz/questions/${id}`)
          .then(() => {
            this.getQuestions();
          })
          .catch(error => {
            console.error("Eroare la ștergere întrebare:", error);
          });
      }
    }
  },
  mounted() {
    this.getQuestions();
  }
};
</script>
<style scoped>
/* ===== General Layout ===== */
div.p-6 {
  padding: 32px 16px;
  font-family: 'Segoe UI', Roboto, sans-serif;
  background-color: #f5f5f5;
  color: #333;
  min-height: 100vh;
}

/* ===== Heading-uri ===== */
h2 {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  text-align: center;
}

h3 {
  font-size: 18px;
  font-weight: 500;
  margin-bottom: 16px;
}

/* ===== Container întrebări și formular ===== */
.bg-white {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  margin: 0 auto 24px auto;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
}

.bg-white.max-w-3xl {
  max-width: 420px;
}

.bg-white.max-w-4xl {
  max-width: 720px;
}

/* ===== Inputuri ===== */
input[type="text"],
input[type="number"] {
  width: 100%;
  padding: 10px;
  font-size: 14px;
  margin-bottom: 12px;
  border: none;
  border-bottom: 2px solid #ccc;
  background-color: transparent;
  transition: border-color 0.2s;
}

input:focus {
  border-color: #4285f4;
  outline: none;
}

/* ===== Răspunsuri (input + scor + buton) ===== */
.flex {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}

.flex-1 {
  flex: 1;
}

/* ===== Butoane ===== */
button {
  font-size: 14px;
  padding: 8px 14px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.bg-blue-500 {
  background-color: #4285f4;
  color: white;
}

.bg-blue-600:hover {
  background-color: #3367d6;
}

.bg-green-600 {
  background-color: #34a853;
  color: white;
}

.bg-green-700:hover {
  background-color: #1e8e3e;
}

/* ===== X pentru ștergere răspuns ===== */
.text-red-500 {
  background: none;
  color: #ea4335;
  font-weight: bold;
  border: none;
  padding: 0 6px;
  cursor: pointer;
}

.text-red-500:hover {
  text-decoration: underline;
  color: #c5221f;
}

/* ===== Liste și text general ===== */
ul.list-disc {
  margin-left: 20px;
  color: #555;
  font-size: 14px;
}

.text-gray-500 {
  color: #777;
  font-style: italic;
  text-align: center;
}
</style>

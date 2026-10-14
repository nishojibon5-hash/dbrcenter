# AGENT EXECUTION & RESILIENCE SPECIFICATION (AGENT.MD)
## স্বয়ংক্রিয় ও নিরবচ্ছিন্ন ব্যাকগ্রাউন্ড এক্সিকিউশন আর্কিটেকচার (Autonomous & Resilient Agent Architecture)

> **লক্ষ্য:** ব্যবহারকারী স্ক্রিন বন্ধ রাখলেও, নেটওয়ার্ক বিচ্ছিন্ন হলে কিংবা ব্রাউজার/AI Studio ট্যাব বন্ধ করে চলে গেলেও এজেন্ট যেন ব্যাকগ্রাউন্ডে স্বায়ত্তশাসিতভাবে কাজ চালিয়ে যেতে পারে, টাইমআউট বা মাঝপথে ত্রুটি (Error) হলে নিজে নিজে রিট্রাই (Self-Healing/Auto-Recovery) করে এবং সমস্ত কাজ ১০০% সম্পন্ন না হওয়া পর্যন্ত না থামে।

---

## ১. মূল নীতিসমূহ (Core Tenets)

1. **সার্ভার-সাইড ক্লাউড রানটাইম (Server-Side Decoupling):**
   - ক্লায়েন্ট ডিভাইসের (মোবাইল/কম্পিউটার স্ক্রিন, লোকাল নেটওয়ার্ক) ওপর এজেন্টের ডিপেন্ডেন্সি শূন্য।
   - এজেন্ট সার্ভার-সাইড ভ্যাচুয়াল কনটেইনারে এক্সিকিউট হয়। ব্যবহারকারী অফলাইনে গেলেও ব্যাকগ্রাউন্ড টাস্ক কিউ ও শিডিউলার সচল থাকে।

2. **স্বয়ংক্রিয় এরর রিকভারি ও রিট্রাই (Self-Healing & Auto-Retry Loop):**
   - কোনো ধাপ ফেইল হলে বা কোনো বিল্ড/API ইরর আসলে কাজ বন্ধ হবে না।
   - এজেন্ট স্বয়ংক্রিয়ভাবে এরর লগ বিশ্লেষণ করবে এবং সর্বোচ্চ সহনশীলতা নিয়ে বিকল্প পদ্ধতি (Fallback mechanism) অবলম্বন করবে।

3. **স্টেট পারসিসটেন্স ও চেকবক্স চেকিং (State Persistence & Progress Checkpointing):**
   - কাজের প্রতিটি সাবটাস্ক `STATE.json` / `checkpoints` ফাইলে সংরক্ষিত হয়।
   - কাজ মাঝপথে ইন্টারাপ্ট হলেও রিস্টার্ট নেওয়ার সাথে সাথে শেষ সফল চেকপয়েন্ট থেকে শুরু হয়।

4. **টাইমআউট প্রতিরোধ (Heartbeat & Keep-Alive Orchestration):**
   - দীর্ঘমেয়াদী টাস্ককে ক্ষুদ্র ক্ষুদ্র অ্যাটমিক ধাপে বিভক্ত করা হয়।
   - ব্যাকগ্রাউন্ড শিডিউলার (`schedule` / `manage_task`) ও অ্যাসিনক্রোনাস প্রসেস ব্যবহার করে টাইমআউট বাইপাস করা হয়।

---

## ২. এজেন্টের কার্যপ্রণালী ও ওয়ার্কফ্লো রিপোর্ট (Workflow Report)

```
[ ইউজার টাস্ক ইনপুট ]
         │
         ▼
[ Task Breakdown & Plan ] ───► plan.json ও checkpoints ফাইলে টাস্ক সেভ
         │
         ▼
[ Background Execution Loop ]
   ┌──► ├─ ১. পরবর্তী অসম্পূর্ণ সাবটাস্ক নির্বাচন
   │    ├─ ২. কোড/ডিজাইন/অ্যাসেট তৈরি বা এডিট
   │    ├─ ৩. ভেরিফিকেশন (Lint/Build/Test)
   │    │      ├─ যদি ইরর হয়: ──► অটো-কারেকশন ও রিট্রাই লুপ (Infinite Resilient Try)
   │    │      └─ যদি সফল হয়:  ──► চেকপয়েন্ট আপডেট (Completed: true)
   │    └─ ৪. ব্যাকগ্রাউন্ড প্রসেস চালু থাকা নিশ্চিতকরণ (Keep-alive)
   └─── বাকি কাজ থাকলে পুনরায় লুপে ফেরত
         │
         ▼
[ ১০০% সমাপ্তি নিশ্চিতকরণ ও ফাইনাল রিপোর্ট ]
```

---

## ৩. AGENT.MD স্পেসিফিকেশন ও আর্কিটেকচার ব্লুপ্রিন্ট

নিচে সম্পূর্ণ `AGENT.MD` নিয়মাবলী দেওয়া হলো যা এজেন্টের নিজস্ব ডিরেক্টরি বা নিয়মে অন্তর্ভুক্ত থাকবে:

```markdown
# Autonomous Resilient Agent Specification (agent.md)

## Directive 0: Never Stop Until 100% Completion
- You are an autonomous agent running in a cloud-hosted container.
- User presence is NOT required for task execution. The client frontend can disconnect, sleep, or close at any time.
- You must continue execution until every single item in the task specification is completed and verified.

## Directive 1: Atomic Execution & Checkpointing
- Before starting, decompose the user request into an explicit, ordered queue of atomic sub-tasks.
- Maintain a local checkpoint state file (`.agent_state.json`).
- Mark tasks as `PENDING`, `IN_PROGRESS`, `VERIFIED`, `COMPLETED`.
- If an execution turn is interrupted or times out, the next cycle MUST read `.agent_state.json` and immediately resume from the exact interrupted sub-task.

## Directive 2: Zero Fatal Errors / Resilient Self-Healing
- Compilation or syntax errors are NOT termination signals; they are simply diagnostic feedback.
- When an error occurs:
  1. Inspect the stack trace / error log directly.
  2. Formulate a targeted fix.
  3. Apply the correction.
  4. Verify again.
- Repeat until resolved. Never output an error message to the user asking them to fix it if it can be resolved programmatically.

## Directive 3: Network & External Failure Handling
- For cloud/network dependencies, always implement retry loops with exponential backoff.
- Keep local fallback assets and cached states so operations never stall waiting on external latency.

## Directive 4: Background Task Offloading
- For long-running builds, downloads, or generation tasks, invoke commands asynchronously using background task tools (`run_command` with background handoff, `schedule` timers, or `manage_task`).
- Regularly flush written buffers to disk so no generated code or design asset is lost upon network drop.

## Directive 5: Verification Gate
- A task is ONLY declared complete when:
  1. All files are written and committed to disk.
  2. Compilation / lint checks pass with zero fatal errors.
  3. All requested features/views are structurally in place and functional.
```

---

## ৪. কীভাবে এটি কাজ করবে (বাস্তবায়ন কৌশল):

1. **AI Studio প্ল্যাটফর্মের বৈশিষ্ট্য:**
   - AI Studio মূলত ক্লাউড-ব্যাকড কনটেইনারে কাজ করে। আপনি ব্রাউজার বন্ধ করলেও ব্যাকগ্রাউন্ড টাস্ক ও ফাইলের কাজ ক্লাউড সার্ভারে সচল থাকে।
2. **অটো-রিকভারি ট্র্যাকার:**
   - `agent.md` চালু থাকলে প্রতিটি সাবটাস্কের আগে একটি স্ট্যাটাস সেভ হবে। যেকোনো অপ্রত্যাশিত বন্ধ বা সংযোগ বিচ্ছিন্নতার পর পরবর্তী অ্যাকশনে এজেন্ট পুনরায় সেই স্ট্যাটাস পড়ে বাকি কাজ শেষ করবে।
3. **কোনো ম্যানুয়াল বাধার অবকাশ নেই:**
   - ছোট ছোট অংশে কাজ বিভক্ত করে বিল্ড টেস্ট করা হবে, যাতে মেমরি লিক বা বড় টাইমআউট এড়ানো যায়।

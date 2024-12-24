package com.anji.kafkademo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;


@Profile("spring")
public class SpringBatchConfig {

    @Value("input/record.csv")

    private Resource inputCsv;

    @Value("file:xml/output.xml")
    private Resource outputXml;

    @Bean
    public ItemReader<Transaction> itemReader()
      throws UnexpectedInputException, ParseException {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<Transaction>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        String[] tokens = { "username", "userid", "transactiondate", "amount" };
        tokenizer.setNames(tokens);
        reader.setResource(inputCsv);
        DefaultLineMapper<Transaction> lineMapper = 
          new DefaultLineMapper<Transaction>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new RecordFieldSetMapper());
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemProcessor<Transaction, Transaction> itemProcessor() {
        return new CustomItemProcessor();
    }

    @Bean
    public ItemWriter<Transaction> itemWriter(Marshaller marshaller)
      throws MalformedURLException {
        StaxEventItemWriter<Transaction> itemWriter = 
          new StaxEventItemWriter<Transaction>();
        itemWriter.setMarshaller(marshaller);
        itemWriter.setRootTagName("transactionRecord");
        itemWriter.setResource(outputXml);
        return itemWriter;
    }

    @Bean
    public Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(new Class[] { Transaction.class });
        return marshaller;
    }

    @Bean
    protected Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, ItemReader<Transaction> reader, 
      ItemProcessor<Transaction, Transaction> processor, ItemWriter<Transaction> writer ) {
        return new StepBuilder("step1", jobRepository).<Transaction, Transaction> chunk(10, transactionManager)
          .reader(reader).processor(processor).writer(writer).build();
    }

    @Bean(name = "firstBatchJob")
    public Job job(JobRepository jobRepository, @Qualifier("step1") Step step1) {
        return new JobBuilder("firstBatchJob", jobRepository).preventRestart().start(step1).build();
    }
    
    public DataSource dataSource() {
     EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
     return builder.setType(EmbeddedDatabaseType.H2)
       .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
       .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
       .build();
    }
    
    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        return new ResourcelessTransactionManager();
    }
    
    @Bean(name = "jobRepository")
    public JobRepository getJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource());
        factory.setTransactionManager(getTransactionManager());
        factory.afterPropertiesSet();
        return factory.getObject();
    }
    
    @Bean(name = "jobLauncher")
    public JobLauncher getJobLauncher() throws Exception {
       TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
       jobLauncher.setJobRepository(getJobRepository());
       jobLauncher.afterPropertiesSet();
       return jobLauncher;
    }
}